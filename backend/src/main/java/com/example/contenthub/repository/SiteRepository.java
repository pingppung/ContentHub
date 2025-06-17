package com.example.contenthub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.contenthub.domain.Site;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    Site findByPlatform(String platform);

    Site findByPlatformAndCategory(String platform, String category);
}