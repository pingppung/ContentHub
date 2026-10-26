package com.example.contenthub.content.repository;

import java.util.List;

import com.example.contenthub.content.Content;
import com.example.contenthub.content.enums.ContentType;

public interface ContentSearchRepository {

    List<Content> search(
            ContentType type,
            String genre,
            String title
    );
}