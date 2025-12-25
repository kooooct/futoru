package org.example.futoru.dto;

import lombok.Data;

@Data
public class BmrRequest {
    private Double height;
    private Double weight;
    private Integer age;
    private Gender gender;
    private ActivityLevel activityLevel;
}
