package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "NovelSite")
public class NovelSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "novel_id", referencedColumnName = "novel_id")
    private Novel novel;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "site_id", referencedColumnName = "site_id")
    private Site site;

    @Column(name = "site_proudct_id")
    private String productId;
}
