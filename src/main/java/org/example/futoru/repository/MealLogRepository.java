package org.example.futoru.repository;

import org.example.futoru.entity.MealLog;
import org.example.futoru.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;



public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    // ユーザーかついつからいつまでを指定して検索
    List<MealLog> findByUserAndEatenAtBetween(User user, LocalDateTime start, LocalDateTime end);
}
