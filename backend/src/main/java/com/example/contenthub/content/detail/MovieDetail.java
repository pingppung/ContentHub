package com.example.contenthub.content.detail;

import java.util.List;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetail implements ContentDetail {

    // ISO 3166-1 alpha-2 codes when available; no guessed country mapping.
    private List<String> countries;
    private List<String> actors;
    private Integer releaseYear;
    private Integer durationMinutes;
    private List<String> directors;
    // Earliest original theatrical release, not a platform availability date.
    // Leave null when the source only provides a year or an unspecified regional date.
    private LocalDate releaseDate;
}
