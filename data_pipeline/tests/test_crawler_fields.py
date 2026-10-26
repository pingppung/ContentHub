import unittest
from unittest.mock import Mock

from selenium.common.exceptions import NoSuchElementException

from data_pipeline.collectors.crawler_collector import CrawlerCollector


class CrawlerFieldsTest(unittest.TestCase):

    def setUp(self):
        self.collector = CrawlerCollector.__new__(CrawlerCollector)
        self.collector.driver = Mock()
        self.collector.seen_ids = set()

    def test_collects_every_configured_field(self):
        self.collector.settings = {
            "fields": {
                "directors": {"xpath": "//director", "type": "list"},
                "runtime": {"xpath": "//runtime", "type": "text"},
                "poster": {
                    "xpath": "//poster",
                    "type": "attribute",
                    "attribute": "src",
                },
                "available": {"xpath": "//available", "type": "exists"},
            },
        }
        director = Mock()
        director.text = "감독"
        text_element = Mock()
        text_element.text = "120"
        text_element.get_attribute.return_value = "poster.jpg"
        self.collector.driver.find_elements.return_value = [director]
        self.collector.driver.find_element.return_value = text_element

        result = self.collector._collect_detail("https://example.com/movie")

        self.assertEqual(result, {
            "directors": ["감독"],
            "runtime": "120",
            "poster": "poster.jpg",
            "available": True,
        })

    def test_optional_missing_field_does_not_skip_content(self):
        self.collector.settings = {
            "fields": {"actors": {"xpath": "//actor", "type": "list"}},
        }
        self.collector.driver.find_elements.return_value = []

        result = self.collector._collect_detail("https://example.com/movie")

        self.assertEqual(result, {"actors": []})

    def test_missing_required_field_skips_content(self):
        self.collector.settings = {
            "fields": {
                "name": {"xpath": "//name", "type": "text", "required": True},
            },
        }
        self.collector.driver.find_element.side_effect = NoSuchElementException()

        self.assertIsNone(self.collector._collect_detail("https://example.com"))

    def test_false_is_valid_required_value(self):
        self.collector.settings = {
            "fields": {
                "adult": {"xpath": "//adult", "type": "exists", "required": True},
            },
        }
        self.collector.driver.find_elements.return_value = []

        self.assertEqual(
            self.collector._collect_detail("https://example.com"),
            {"adult": False},
        )

    def test_text_uses_first_nonempty_xpath(self):
        self.collector._get_text = Mock(side_effect=[None, "소개"])

        result = self.collector._read_field({
            "xpath": ["//first", "//second"],
            "type": "text",
        })

        self.assertEqual(result, "소개")

    def test_transforms_title_and_genre(self):
        title = self.collector._transform_value(
            "[완결] 제목", {"type": "text", "transform": {"remove": r"\[.*?\]"}}
        )
        genre = self.collector._transform_value(
            ["판타지/모험", " 로맨스 "],
            {"type": "list", "transform": {"split": r"[·/,|]"}},
        )

        self.assertEqual(title, "제목")
        self.assertEqual(genre, ["판타지", "모험", "로맨스"])

    def test_unknown_read_type_raises_error(self):
        with self.assertRaises(ValueError):
            self.collector._read_field({"xpath": "//name", "type": "unknown"})

    def test_placeholder_is_not_sent_to_browser(self):
        self.assertIsNone(self.collector._read_field({"xpath": "추가", "type": "list"}))
        self.collector.driver.find_elements.assert_not_called()

    def test_detail_failure_does_not_return_success(self):
        self.collector.settings = {"detail_url": "https://example.com/{id}"}
        self.collector._wait_for_items = Mock(return_value=[Mock()])
        self.collector._extract_id = Mock(return_value="1")
        self.collector._collect_detail = Mock(return_value=None)
        with self.assertRaises(RuntimeError):
            self.collector._collect_items()

    def test_closing_one_collector_only_closes_its_driver(self):
        other = CrawlerCollector.__new__(CrawlerCollector)
        other.driver = Mock()
        self.collector.close()
        self.collector.driver.quit.assert_called_once()
        other.driver.quit.assert_not_called()



if __name__ == "__main__":
    unittest.main()
