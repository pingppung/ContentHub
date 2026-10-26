package com.example.contenthub.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);

    User findByUserIdAndPassword(String userId, String password);

    @Query(value = """
            SELECT DATE_FORMAT(created_at, '%Y-%m') AS date,
                   COUNT(*) AS count
            FROM users
            GROUP BY DATE_FORMAT(created_at, '%Y-%m')
            ORDER BY date
            """, nativeQuery = true)
    List<UserGrowthProjection> getUserGrowth();
}

