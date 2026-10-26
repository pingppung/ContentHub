package com.example.contenthub.content.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {

    private List<FieldCount> categoryCounts;
    private List<DailyCount> userGrowth;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldCount {
        private String field;
        private int count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyCount {
        private String date;
        private int count;
    }

}
