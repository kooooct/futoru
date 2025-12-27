package org.example.futoru.service;

import lombok.RequiredArgsConstructor;
import org.example.futoru.entity.FoodItem;
import org.example.futoru.entity.MealLog;
import org.example.futoru.entity.User;
import org.example.futoru.repository.FoodItemRepository;
import org.example.futoru.repository.MealLogRepository;
import org.example.futoru.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 食事記録および食品マスタに関するビジネスロジックを管理するサービスクラス。
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {

    private final FoodItemRepository foodItemRepository;
    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;

    /**
     * ユーザーが選択可能な食品リストを取得します。
     * システム標準の食品（user_idがNULL）と、ユーザー自身が登録した食品（My食品）の両方を含みます。
     *
     * @param username 現在のユーザー名
     * @return 利用可能な食品リスト
     */
    public List<FoodItem> getAvailableFoods(String username) {
        User user = getUser(username);
        return foodItemRepository.findAllAvailable(user);
    }

    /**
     * 当日の食事記録一覧を取得します。
     * 検索範囲は当日の 00:00:00 から 23:59:59 までとします。
     *
     * @param username 現在のユーザー名
     * @return 今日のMealLogリスト
     */
    public List<MealLog> getTodayMealLogs(String username) {
        User user = getUser(username);

        // 当日の開始・終了時刻を設定して検索範囲を指定
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        return mealLogRepository.findByUserAndEatenAtBetween(user, start, end);
    }

    /**
     * 食品マスタ（FoodItem）を選択して食事を記録します。
     * マスタデータの変更に影響されないよう、記録時点での食品名とカロリーをスナップショットとして保存します。
     *
     * @param username   現在のユーザー名
     * @param foodItemId 選択された食品マスタID
     * @param amount     摂取量（単位に対する倍率）
     */
    public void recordMealFromMaster(String username, Long foodItemId, Double amount) {
        User user = getUser(username);
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid food item ID"));

        MealLog log = new MealLog();
        log.setUser(user);
        log.setFoodItem(foodItem); // マスタとの紐付けを保持

        // スナップショット保存: マスタの内容が変わっても履歴が変わらないように値をコピー
        log.setName(foodItem.getName());

        // カロリー計算: 基準値 * 量
        int totalCalories = (int) (foodItem.getCalories() * amount);
        log.setCalories(totalCalories);

        log.setAmount(amount);
        log.setEatenAt(LocalDateTime.now());

        mealLogRepository.save(log);
    }

    /**
     * 食品マスタを使用せず、手入力で食事を記録します。
     *
     * @param username 現在のユーザー名
     * @param name     食品名
     * @param calories 合計カロリー
     */
    public void recordManualMeal(String username, String name, int calories) {
        User user = getUser(username);

        MealLog log = new MealLog();
        log.setUser(user);
        log.setFoodItem(null); // マスタ紐付けなし
        log.setName(name);
        log.setCalories(calories);
        log.setAmount(1.0); // 手入力の場合は便宜上1.0とする
        log.setEatenAt(LocalDateTime.now());

        mealLogRepository.save(log);
    }

    /**
     * 指定された食事記録を削除します。
     * 他人のデータを削除できないよう、所有者チェックを行います。
     *
     * @param logId    削除対象のログID
     * @param username リクエストしたユーザー名
     * @throws SecurityException 所有者でない場合にスロー
     */
    public void deleteMealLog(Long logId, String username) {
        MealLog log = mealLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Log not found"));

        if (!log.getUser().getUsername().equals(username)) {
            throw new SecurityException("You cannot delete this log");
        }
        mealLogRepository.delete(log);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}