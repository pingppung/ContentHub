package com.example.contenthub.content.dto.request;

import com.example.contenthub.content.enums.ContentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentCollectRequest {
    private String platform;
    private ContentType type;
}
