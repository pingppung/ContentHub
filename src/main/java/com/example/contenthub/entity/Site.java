package com.example.contenthub.entity;

import com.example.contenthub.constants.SiteType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.annotations.One;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private Long site_id;

    @JoinColumn(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "site")
    private List<NovelSite> sites = new ArrayList<>();
}