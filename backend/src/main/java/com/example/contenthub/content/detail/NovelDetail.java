package com.example.contenthub.content.detail;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NovelDetail implements ContentDetail {

    private List<String> authors;

    private LocalDate firstPublishedDate;
}
