package org.example.futoru.repository;

import org.example.futoru.entity.FoodItem;
import org.example.futoru.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    // 自分のMy食品 + システム標準(user is null) を両方取得する必殺技
    @Query("SELECT f FROM FoodItem f WHERE f.user = :user OR f.user IS NULL")
    List<FoodItem> findAllAvailable(@Param("user") User user);

    // 特定のユーザーが作ったものだけ探す（管理用など）
    List<FoodItem> findByUser(User user);
}
