package com.example.contenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.contenthub.entity.Site;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    Site findByPlatform(String platform);

}