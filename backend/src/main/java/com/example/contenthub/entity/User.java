package com.example.contenthub.entity;

import java.sql.Timestamp;
import java.util.Set;

import com.example.contenthub.constants.RoleType;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @ElementCollection(targetClass = RoleType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<RoleType> roles;

    @PrePersist
    public void setDefaultProfileImage() {
        if (this.profileImageUrl == null) {
            this.profileImageUrl = "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp"; // 기본 프로필 이미지 URL 설정
        }
    }
}
