package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor 
@Table(name = "novels")
public class Novel extends Content {

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NovelLink> links = new ArrayList<>();

    public void addLink(NovelLink link) {
        links.add(link);
        link.setContent(this);
    }

    public void removeLink(NovelLink link) {
        links.remove(link);
        link.setContent(null);
    }

    public Novel(String title, String summary, String coverImg, boolean adultContent, String genre) {
        super(title, summary, coverImg, adultContent, genre);
    }
}