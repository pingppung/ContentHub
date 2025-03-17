package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "contents_and_sites")
public class ContentSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "cid", nullable = false)
    private String contentID;

    @Column(name = "is_adult_content")
    private boolean isAdult;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    public ContentSite(String contentID, boolean isAdult, Content content, Site site) {
        this.contentID = contentID;
        this.isAdult = isAdult;
        this.content = content;
        this.site = site;
    }

}
