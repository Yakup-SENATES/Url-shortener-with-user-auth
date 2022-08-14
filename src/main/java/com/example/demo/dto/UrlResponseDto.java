package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponseDto {
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime expirationDate;
}
