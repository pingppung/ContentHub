import requests

from data_pipeline.collectors.base import ContentCollector


class ApiCollector(ContentCollector):

    def __init__(self, settings: dict):
        self.settings = settings

    def collect(self):
        request = self.settings["list_request"].copy()
        request["params"] = dict(request.get("params") or {})
        pagination = self.settings.get("pagination")
        if pagination:
            if pagination["type"] != "offset":
                raise ValueError("지원하지 않는 API 페이지 방식입니다.")
            if pagination["step"] <= 0:
                raise ValueError("페이지 이동 간격은 양수여야 합니다.")

        results = []
        seen_ids = set()

        page_count = 0
        while True:
            page_count += 1
            if pagination and page_count > pagination.get("max_pages", 1000):
                raise RuntimeError("최대 API 페이지 수를 초과했습니다.")
            list_data = self._request(request)
            items = self._extract_list_items(list_data, self.settings["list_paths"])
            if not items:
                break

            new_count = 0

            for item in items:
                item_id = self._get_path_value(item, self.settings["id_path"])
                if item_id is None or not str(item_id).strip():
                    raise ValueError("API 목록 항목에 ID가 없습니다.")

                item_id = str(item_id)
                if item_id in seen_ids:
                    continue

                seen_ids.add(item_id)
                new_count += 1
                detail_data = {}
                detail_request = self.settings.get("detail_request")

                if detail_request:
                    detail_data = self._request(
                        self._build_detail_request(detail_request, item_id)
                    )

                result = self._map_fields(item, detail_data)
                result["id"] = item_id
                results.append(result)

            if not pagination:
                break

            if new_count == 0:
                raise ValueError("페이지를 이동했지만 새로운 API 항목이 없습니다.")

            param = pagination["param"]
            request["params"][param] = (
                request["params"].get(param, 0) + pagination["step"]
            )

        return results

    def _request(self, request_settings: dict):
        response = requests.request(
            method=request_settings.get("method", "GET"),
            url=request_settings["url"],
            params=request_settings.get("params"),
            headers=request_settings.get("headers"),
            timeout=10,
        )
        response.raise_for_status()
        return response.json()

    def _build_detail_request(self, request_settings: dict, item_id):
        request = request_settings.copy()
        request["url"] = request["url"].format(id=item_id)
        request["params"] = {
            key: value.format(id=item_id) if isinstance(value, str) else value
            for key, value in (request_settings.get("params") or {}).items()
        }
        return request

    def _extract_list_items(self, data: dict, paths: list[str]):
        items = []

        for path in paths:
            value = self._get_path_value(data, path)
            if not isinstance(value, list):
                raise ValueError(f"API 목록 응답 경로가 올바르지 않습니다: {path}")
            items.extend(value)

        return items

    def _map_fields(self, list_data: dict, detail_data: dict):
        sources = {"list": list_data, "detail": detail_data}
        result = {}

        for name, field in self.settings["fields"].items():
            source = field["source"]
            if source not in sources:
                raise ValueError(f"지원하지 않는 API 응답 위치입니다: {source}")

            value = self._get_path_value(sources[source], field["path"])

            if isinstance(value, bool) and "values" in field:
                value = field["values"]["true" if value else "false"]

            if field.get("type") == "list" and value is not None:
                value = value if isinstance(value, list) else [value]

            if field.get("required") and (value is None or value == "" or value == []):
                raise ValueError(f"API 필수 필드가 없습니다: {name}")
            result[name] = value

        return result

    def _get_path_value(
        self,
        data,
        path: str
    ):
        if not path:
            return None

        parts = path.split(".")
        current = data

        for part in parts:

            if part.endswith("[]"):
                key = part[:-2]

                if not isinstance(current, dict):
                    return None

                current = current.get(key)

                if not isinstance(current, list):
                    return None

                continue

            if isinstance(current, list):
                current = [
                    item.get(part)
                    for item in current
                    if isinstance(item, dict)
                ]

            elif isinstance(current, dict):
                current = current.get(part)

            else:
                return None

            if current is None:
                return None

        return current

