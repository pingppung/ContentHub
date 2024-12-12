package com.example.contenthub.repository;

import com.example.contenthub.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    // List<Content> findByGenre(String genre);

    // Content findByTitle(String title);

    // void deleteByTitle(String title);

    // List<Content> findByTitleContaining(String word);


}
