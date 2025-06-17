import requests
import importlib
import re
from urllib.parse import urlencode


def load_config_module(platform_name: str):
    try:
        return importlib.import_module(f"api_collector.configs.{platform_name}_config")
    except ModuleNotFoundError:
        raise ValueError(f"지원하지 않는 플랫폼입니다: {platform_name}")


def build_url(config_module, content_type: str) -> str:
    COMMON_PARAMS = config_module.COMMON_PARAMS
    CONTENT_CONFIG = config_module.CONTENT_CONFIG

    config = CONTENT_CONFIG.get(content_type)
    if not config:
        raise ValueError(f"콘텐츠 타입 오류: {content_type}에 맞는 설정이 없습니다")

    base_url = config["base_url"]
    specific_params = config["params"]
    all_params = {**COMMON_PARAMS, **specific_params}
    return f"{base_url}?{urlencode(all_params)}"


def extract_cid_list(data: dict, content_conf: dict) -> list:
    def get_parsed_list(json_data, path):
        d = json_data
        for key in path:
            d = d.get(key, {})
        return d if isinstance(d, list) else []

    def extract_content_id(item, extraction_rule):
        source = item.get(extraction_rule["list_key"], {})

        if isinstance(source, list):
            index = extraction_rule.get("index", 0)
            if len(source) > index:
                target = source[index].get(extraction_rule["target_key"])

                extract_regex = extraction_rule.get("extract_regex")
                if extract_regex and isinstance(target, list):
                    for s in target:
                        m = re.search(extract_regex, s)
                        if m:
                            return m.group(1)
                    return None
                return target

        elif isinstance(source, dict):
            return source.get(extraction_rule["target_key"])
        return None

    items = get_parsed_list(data, content_conf["parse_path"])
    extraction_rule = content_conf.get("cid_extraction")

    content_ids = []
    for item in items:
        cid = extract_content_id(item, extraction_rule)
        if cid:
            content_ids.append(cid)

    return content_ids


def build_endpoint_url(content_id: str, content_conf: dict) -> str:
    endpoint_url = content_conf["endpoint_url"]
    return endpoint_url.format(cid=content_id)


def get_by_path(data, path):
    keys = path.split(".")
    for key in keys:
        if isinstance(data, list):
            data = [d.get(key) for d in data]
        else:
            data = data.get(key, {})
    return data


def extract_field(data, path):
    if "[]" in path:
        pre_path, last_key = path.split("[]")
        items = get_by_path(data, pre_path.rstrip("."))
        return [item.get(last_key.strip(".")) for item in items or [] if item]
    elif "image" in path:
        img = get_by_path(data, path)
        if img and not img.startswith("http"):
            return "https://" + img
        return img
    elif path == "releaseyear":
        return get_status(data.get(path))
    elif path == "releasedate":
        return data.get("firstreleasedate") or data.get(path)
    else:
        return get_by_path(data, path)


def get_status(release_year: str) -> str:
    if "~" in release_year:
        start, end = release_year.split("~")
        return "종영" if end.strip() else "방영 중"
    return "정보 없음"


def fetch_metadata(url: str, content_conf: dict):
    try:
        response = requests.get(url)
        if response.status_code == 200:
            data = response.json()
            result = {}
            field_mapping = content_conf["field_mapping"]
            for key, path in field_mapping.items():
                result[key] = extract_field(data, path)
            return result
        else:
            print(f"요청 실패: {url} - {response.status_code}")
            return None
    except Exception as e:
        print(f"에러: {url} - {e}")
        return None


def collect_content(platform_name: str, content_type: str):
    print(platform_name)
    config_module = load_config_module(platform_name)
    url = build_url(config_module, content_type)
    response = requests.get(url)

    if response.status_code != 200:
        print(f"요청 실패: {response.status_code}")
        return {"error": f"HTTP {response.status_code}"}

    data = response.json()

    content_conf = config_module.CONTENT_CONFIG[content_type]

    content_ids = extract_cid_list(data, content_conf)

    endpoint_urls = [build_endpoint_url(cid, content_conf) for cid in content_ids]

    results = [fetch_metadata(url, content_conf) for url in endpoint_urls]
    return results
