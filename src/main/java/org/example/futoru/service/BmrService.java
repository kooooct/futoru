package org.example.futoru.service;

import lombok.RequiredArgsConstructor;
import org.example.futoru.dto.ActivityLevel;
import org.example.futoru.dto.BmrRequest;
import org.example.futoru.dto.BmrResponse;
import org.example.futoru.dto.Gender;
import org.example.futoru.entity.User;
import org.example.futoru.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 基礎代謝および目標カロリーの計算を行うサービスクラス。
 */
@Service
@RequiredArgsConstructor
public class BmrService {

    private final UserRepository userRepository;

    /**
     * 登録済みユーザー情報に基づき、目標カロリーを計算して返却する。
     * プロフィール情報が不足している場合はデフォルト値を返す。
     *
     * @param username 対象のユーザー名
     * @return 1日の目標摂取カロリー (kcal)
     */
    public int calculateTargetCalories(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getWeight() == null || user.getHeight() == null || user.getAge() == null) {
            return 2200; // デフォルト値
        }

        BmrRequest request = new BmrRequest();
        request.setHeight(Double.valueOf(user.getHeight()));
        request.setWeight(user.getWeight());
        request.setAge(user.getAge());
        request.setGender(convertGender(user.getGender()));
        request.setActivityLevel(convertActivityLevel(user.getActivityLevel()));

        BmrResponse response = calculate(request);

        return (int) response.getTargetCalories();
    }

    /**
     * リクエストDTOに基づきBMR、TDEE、目標カロリーを計算する。
     *
     * @param request 計算用パラメータ
     * @return 計算結果レスポンス
     */
    public BmrResponse calculate(BmrRequest request){
        double bmr = calculateBmr(request);
        double tdee = calculateTdee(bmr, request.getActivityLevel());
        double targetCalories = tdee + 300; // 増量用サープラス設定

        BmrResponse response = new BmrResponse();
        response.setBmr(Math.round(bmr * 10.0) / 10.0);
        response.setTdee(Math.round(tdee * 10.0) / 10.0);
        response.setTargetCalories(Math.round(targetCalories * 10.0) / 10.0);
        response.setDescription(createAdviceMessage(targetCalories));

        return response;
    }

    private double calculateBmr(BmrRequest req){
        double baseResult = (10 * req.getWeight()) + (6.25 * req.getHeight()) - (5 * req.getAge());
        if (req.getGender() == Gender.MALE) {
            return baseResult + 5;
        } else {
            return baseResult - 161;
        }
    }

    private double calculateTdee(double bmr, ActivityLevel level) {
        if (level == null) return bmr * 1.2;
        switch (level) {
            case LOW: return bmr * 1.375;
            case MID: return bmr * 1.55;
            case HIGH: return bmr * 1.725;
            default: return bmr * 1.2;
        }
    }

    private String createAdviceMessage(double target) {
        return "太るためには、1日約 " + (int)target + "kcal を目指して食べましょう！Futoruと一緒に頑張りましょう。";
    }

    private Gender convertGender(String genderStr) {
        if (genderStr == null) return Gender.MALE;
        if (genderStr.equalsIgnoreCase("MALE") || genderStr.equals("男性")) return Gender.MALE;
        return Gender.FEMALE;
    }

    private ActivityLevel convertActivityLevel(String levelStr) {
        if (levelStr == null) return ActivityLevel.LOW;
        try {
            return ActivityLevel.valueOf(levelStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ActivityLevel.LOW;
        }
    }
}