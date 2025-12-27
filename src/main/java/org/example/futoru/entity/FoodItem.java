package org.example.futoru.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "food_items")
@Data
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne@JoinColumn(name = "user_id")
    private User user; // null

    private String name;
    private Integer calories; // 基準カロリー
    private String unit;      // 単位
    private String type;      // INGREDIENT, DISH, PRODUCT
}
