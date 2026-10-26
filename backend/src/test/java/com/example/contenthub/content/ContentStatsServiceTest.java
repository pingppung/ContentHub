package com.example.contenthub.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.contenthub.content.dto.response.StatisticsResponse.DailyCount;
import com.example.contenthub.user.UserGrowthProjection;
import com.example.contenthub.user.UserRepository;

class ContentStatsServiceTest {

    @Test
    void userGrowthUsesProjectedMonthAsDate() {
        UserRepository users = mock(UserRepository.class);
        UserGrowthProjection growth = mock(UserGrowthProjection.class);
        when(growth.getDate()).thenReturn("2026-10");
        when(growth.getCount()).thenReturn(12L);
        when(users.getUserGrowth()).thenReturn(List.of(growth));
        ContentStatsService service = new ContentStatsService(mock(MongoTemplate.class), users);

        List<DailyCount> result = service.getUserGrowth();

        assertEquals(1, result.size());
        assertEquals("2026-10", result.get(0).getDate());
        assertEquals(12, result.get(0).getCount());
    }
}
