package com.example.contenthub.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column (name = "name")
    private String userName;

    @Column (name = "password")
    private String userPwd;
}
