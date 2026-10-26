import unittest

from data_pipeline.converters.content_converter import (
    normalize_collected_content, normalize_days, as_number, normalize_date,
)


class ContentConverterTest(unittest.TestCase):

    def test_normalizes_values_and_keeps_extra_fields(self):
        result = normalize_collected_content({
            "id": "1", "title": " 작품 ", "status": "연재중",
            "episode_count": "총 1,200화", "release_days": ["월요일", "금요일"],
            "first_published_date": "2024.01.02", "publisher": "출판사",
            "rating": "9.5", "rating_max": "10", "custom_field": "원본",
        }, "NOVEL")
        self.assertEqual(result["episode_count"], 1200)
        self.assertEqual(result["status"], "ONGOING")
        self.assertEqual(result["release_days"], ["MONDAY", "FRIDAY"])
        self.assertEqual(result["first_published_date"], "2024-01-02")
        self.assertEqual(result["extra_fields"]["publisher"], "출판사")
        self.assertEqual(result["extra_fields"]["custom_field"], "원본")

    def test_does_not_invent_a_date_from_year(self):
        with self.assertRaises(ValueError):
            normalize_date("2024")

    def test_unknown_days_raise_error(self):
        with self.assertRaises(ValueError):
            normalize_days(["날짜 미정"])

    def test_numbers_reject_negative_and_fractional_count(self):
        for value in ("-1", "1.5", True):
            with self.assertRaises(ValueError):
                as_number(value, integer=True)

    def test_keeps_unknown_date_in_extra_fields(self):
        result = normalize_collected_content({
            "id": "1", "title": "영화", "source_release_date": "2024",
        }, "MOVIE")
        self.assertIsNone(result["release_date"])
        self.assertEqual(result["extra_fields"]["source_release_date"], "2024")


if __name__ == "__main__":
    unittest.main()
