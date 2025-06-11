package com.example.contenthub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectRequestDTO {
    private List<String> platforms;
    private List<String> categories;
}
