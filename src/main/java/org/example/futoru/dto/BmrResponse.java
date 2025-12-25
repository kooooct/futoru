package org.example.futoru.dto;

import lombok.Data;

@Data
public class BmrResponse {
    private Double bmr;
    private Double tdee;
    private Double targetCalories;
    private String description;
}
