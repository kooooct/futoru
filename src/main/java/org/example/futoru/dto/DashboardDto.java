package org.example.futoru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ダッシュボード画面に表示するカロリー情報をまとめたデータクラス。
 */
@Data
@AllArgsConstructor
public class DashboardDto {
    private int targetCalories;    // 目標カロリー
    private int currentCalories;   // 現在の摂取カロリー
    private int remainingCalories; // あと何kcal食べるべきか
}