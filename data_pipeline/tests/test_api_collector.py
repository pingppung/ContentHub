import unittest
from unittest.mock import Mock

from data_pipeline.collectors.api_collector import ApiCollector


class ApiCollectorTest(unittest.TestCase):

    def settings(self):
        return {
            "list_request": {"url": "https://example.com/list", "params": {"offset": 0}},
            "list_paths": ["items"],
            "id_path": "key",
            "detail_request": {"url": "https://example.com/detail/{id}"},
            "fields": {
                "title": {"source": "list", "path": "name"},
                "actors": {"source": "detail", "path": "cast[].name", "type": "list"},
            },
        }

    def test_maps_sources_and_deduplicates_ids(self):
        collector = ApiCollector(self.settings())
        collector._request = Mock(side_effect=[
            {"items": [{"key": 1, "name": "작품"}, {"key": 1, "name": "중복"}]},
            {"cast": [{"name": "배우"}]},
        ])

        self.assertEqual(collector.collect(), [
            {"id": "1", "title": "작품", "actors": ["배우"]},
        ])
        self.assertEqual(collector._request.call_count, 2)

    def test_offset_moves_until_empty_without_changing_settings(self):
        settings = self.settings()
        settings["pagination"] = {"type": "offset", "param": "offset", "step": 2}
        collector = ApiCollector(settings)
        offsets = []

        def request(config):
            if config["url"].endswith("/list"):
                offset = config["params"]["offset"]
                offsets.append(offset)
                return {"items": [{"key": offset, "name": "작품"}]} if offset < 4 else {"items": []}
            return {"cast": []}

        collector._request = Mock(side_effect=request)

        self.assertEqual(len(collector.collect()), 2)
        self.assertEqual(offsets, [0, 2, 4])
        self.assertEqual(settings["list_request"]["params"]["offset"], 0)

    def test_repeated_page_raises_error(self):
        settings = self.settings()
        settings["pagination"] = {"type": "offset", "param": "offset", "step": 1}
        collector = ApiCollector(settings)
        collector._request = Mock(side_effect=[
            {"items": [{"key": 1, "name": "작품"}]},
            {"cast": []},
            {"items": [{"key": 1, "name": "작품"}]},
        ])

        with self.assertRaises(ValueError):
            collector.collect()

    def test_missing_id_raises_error(self):
        collector = ApiCollector(self.settings())
        collector._request = Mock(return_value={"items": [{"name": "작품"}]})

        with self.assertRaises(ValueError):
            collector.collect()

    def test_boolean_status_and_scalar_list(self):
        collector = ApiCollector(self.settings())
        collector.settings["fields"] = {
            "status": {
                "source": "detail", "path": "finish",
                "values": {"true": "COMPLETED", "false": "ONGOING"},
            },
            "artists": {"source": "detail", "path": "author", "type": "list"},
        }

        self.assertEqual(
            collector._map_fields({}, {"finish": False, "author": "작가"}),
            {"status": "ONGOING", "artists": ["작가"]},
        )

    def test_detail_request_formats_params_without_mutation(self):
        collector = ApiCollector(self.settings())
        request = {"url": "https://example.com", "params": {"id": "{id}"}}

        result = collector._build_detail_request(request, "123")

        self.assertEqual(result["params"]["id"], "123")
        self.assertEqual(request["params"]["id"], "{id}")

    def test_detail_failure_is_not_reported_as_success(self):
        collector = ApiCollector(self.settings())
        collector._request = Mock(side_effect=[
            {"items": [{"key": 1, "name": "작품"}]},
            RuntimeError("상세 요청 실패"),
        ])

        with self.assertRaises(RuntimeError):
            collector.collect()

    def test_missing_list_path_is_not_an_empty_result(self):
        collector = ApiCollector(self.settings())
        with self.assertRaises(ValueError):
            collector._extract_list_items({"error": "bad response"}, ["items"])
        self.assertEqual(collector._extract_list_items({"items": []}, ["items"]), [])

    def test_required_title_is_validated(self):
        collector = ApiCollector(self.settings())
        collector.settings["fields"]["title"]["required"] = True
        with self.assertRaises(ValueError):
            collector._map_fields({}, {})



if __name__ == "__main__":
    unittest.main()
