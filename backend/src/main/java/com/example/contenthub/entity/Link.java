package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long linkId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "site_name", nullable = false)
    private String siteName;

    @Column(name = "cid", nullable = false)
    private String contentUId; // 콘텐츠 ID

    public Link(String url, String siteName, String contentUId) {
        this.url = url;
        this.siteName = siteName;
        this.contentUId = contentUId;
    }
}
