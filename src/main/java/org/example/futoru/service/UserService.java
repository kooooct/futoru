package org.example.futoru.service;

import org.example.futoru.entity.User;
import org.example.futoru.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");

        userRepository.save(user);
    }

    /**
     * プロフィールを更新し、目標カロリーを自動計算する
     * @param username 更新するユーザーの名前
     * @param height 身長 (cm)
     * @param weight 体重 (kg)
     * @param age 年齢
     * @param gender 性別 (MALE / FEMALE)
     * @param activityLevel 活動レベル (1~3)
     */
    @Transactional
    public void updateProfile(String username, Double height, Double weight, Integer age, String gender, Integer activityLevel) {
        // 1. ユーザーを取得 (存在しない場合はエラー)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. データをセット
        user.setHeight(height);
        user.setWeight(weight);
        user.setAge(age);
        user.setGender(gender);
        user.setActivityLevel(String.valueOf(activityLevel));

        // 3. 目標カロリーを設定する
        int targetCalories = calculateTargetCalories(height, weight, age, gender, activityLevel);
        user.setTargetCalories(targetCalories);

        // 4. 保存
        userRepository.save(user);
    }

    // カロリー計算ロジック
    private int calculateTargetCalories(Double height, Double weight, Integer age, String gender, Integer activityLevel) {
        double bmr;
        if ("MALE".equals(gender)) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        double tdee;
        switch (activityLevel) {
            case 1: tdee = bmr * 1.2; break; // ほぼ運動しない
            case 2: tdee = bmr * 1.55; break; // 適度な運動
            case 3: tdee = bmr * 1.9; break; // 激しい運動
            default: tdee = bmr * 1.2;
        }

        return (int) (tdee + 300);
    }
}
