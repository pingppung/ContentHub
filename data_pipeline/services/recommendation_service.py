import numpy as np
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


def recommend_contents(data: dict):

    content_type = data.get("type")
    query = data.get("query")
    content_list = data.get("contentList", [])

    if not content_list:
        return {"top_contents": []}

    df = pd.DataFrame(content_list)

    def make_features(content, content_type):
        title = content.get("title") or ""
        synopsis = content.get("synopsis") or ""
        genre = content.get("genre") or []

        text = f"{title} {synopsis} {' '.join(genre)}"

        detail = content.get("detail") or {}

        if content_type == "MOVIE":
            actors = detail.get("actors") or []
            countries = detail.get("countries") or []
            directors = detail.get("directors") or []
            text += f" {' '.join(actors)} {' '.join(countries)} {' '.join(directors)}"

        elif content_type == "DRAMA":
            actors = detail.get("actors") or []
            broadcasters = detail.get("broadcasters") or []
            text += f" {' '.join(actors)} {' '.join(broadcasters)}"

        elif content_type == "WEBTOON":
            writers = detail.get("writers") or []
            artists = detail.get("artists") or []
            text += f" {' '.join(writers)} {' '.join(artists)}"

        elif content_type == "NOVEL":
            text += f" {' '.join(detail.get('authors') or [])}"

        return text.strip()

    query_text = make_features(query, content_type)

    df["features"] = df.apply(
        lambda row: make_features(row.to_dict(), content_type),
        axis=1
    )

    all_docs = df["features"].tolist() + [query_text]
    if not any(doc.strip() for doc in all_docs):
        return {"top_contents": []}

    vectorizer = TfidfVectorizer(
        tokenizer=lambda x: x.split(),
        token_pattern=None
    )

    tfidf = vectorizer.fit_transform(all_docs)

    similarities = cosine_similarity(
        tfidf[-1],
        tfidf[:-1]
    )[0]

    n = min(10, len(df))
    top_indices = np.argsort(similarities)[::-1][:n]

    top_contents = (
        df.iloc[top_indices]
        .drop(columns=["features"])
        .to_dict(orient="records")
    )

    return {
        "top_contents": top_contents
    }
