package com.example.contenthub.content;

import com.example.contenthub.content.dto.response.StatisticsResponse;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import com.example.contenthub.content.dto.response.StatisticsResponse.FieldCount;
import com.example.contenthub.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentStatsService {

    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    // 콘텐츠 타입별 개수
    public List<FieldCount> getContentTypeCount() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("type").count().as("count"),
                Aggregation.project("count").and("_id").as("field"));

        return mongoTemplate.aggregate(agg, "contents", FieldCount.class).getMappedResults();
    }

    // 유저 월별 가입자 수
    public List<StatisticsResponse.DailyCount> getUserGrowth() {
        return userRepository.getUserGrowth()
                .stream()
                .map(result -> new StatisticsResponse.DailyCount(
                        result.getDate(),
                        result.getCount().intValue()
                ))
                .toList();
    }

}
