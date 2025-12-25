package org.example.futoru.dto;

import org.example.futoru.entity.Food;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardDto {
    private List<Food> foods;
    private Integer totalCalories;
    private Integer targetCalories;
    private Integer remainingCalories;
    private Integer progress;
}
