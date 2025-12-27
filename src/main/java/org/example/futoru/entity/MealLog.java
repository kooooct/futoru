package org.example.futoru.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_logs")
@Data
public class MealLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem; // マスタへのリンク(手入力ならnull)

    private String name;       // 記録時の名前
    private Integer calories;  // 記録時の合計カロリー
    private Double amount;     // 食べた量

    @Column(name = "eaten_at", nullable = false)
    private LocalDateTime eatenAt;
}
