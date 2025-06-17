package com.example.contenthub.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sites")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "platform", nullable = false)
    private String platform;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "url_format", nullable = false)
    private String urlFormat;
}
