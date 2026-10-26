package com.example.contenthub.content.detail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DramaDetail implements ContentDetail {

    private Integer releaseYear;

    private List<String> actors;
    private Integer seasonCount;
    // Original broadcast networks, not streaming providers.
    private List<String> broadcasters;

    // Original broadcast schedule; platform release schedules belong to SiteDetail.
    private List<DayOfWeek> broadcastDays;
    // Total planned episodes across all seasons; null if unknown.
    private Integer episodeCount;
    private LocalDate firstAiredDate;
    // Actual final broadcast date; null until known (not an estimated end date).
    private LocalDate lastAiredDate;
}
