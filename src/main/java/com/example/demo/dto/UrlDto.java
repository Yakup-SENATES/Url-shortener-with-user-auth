package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UrlDto {
    private String url;
    private String expirationDate;


}
