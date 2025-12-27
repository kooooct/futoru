package org.example.futoru.repository;

import org.example.futoru.entity.FoodItem;
import org.example.futoru.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // ある料理に使われている具材リストを取得
    List<Recipe> findByParentFood(FoodItem parentFood);
}
