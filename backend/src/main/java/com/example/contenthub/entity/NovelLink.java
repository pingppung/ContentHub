package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "novel_links")
public class NovelLink extends Link {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel content;
  
    public NovelLink(String url, String siteName, String contentUID, Novel novel) {
        super(url, siteName, contentUID);
        this.content = novel; // Novel과 관계 설정
    }
}