package org.example.futoru.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recipes")
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_food_id", nullable = false)
    private FoodItem parentFood;

    @ManyToOne
    @JoinColumn(name = "child_food_id", nullable = false)
    private FoodItem childFood;

    private Double amount;
}
