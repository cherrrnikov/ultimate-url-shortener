package com.cherrrnikov.urlshortener.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LinkResponse {
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
    private Long clickCount;
}
