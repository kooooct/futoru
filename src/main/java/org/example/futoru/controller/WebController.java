package org.example.futoru.controller;

import lombok.RequiredArgsConstructor;
import org.example.futoru.dto.DashboardDto;
import org.example.futoru.entity.FoodItem;
import org.example.futoru.entity.MealLog;
import org.example.futoru.service.BmrService;
import org.example.futoru.service.FoodService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 画面表示（ビュー）を制御するメインコントローラー。
 */
@Controller
@RequiredArgsConstructor
public class WebController {

    private final FoodService foodService;
    private final BmrService bmrService;

    /**
     * ダッシュボード（トップページ）を表示します。
     * 今日の食事記録、カロリー進捗、選択可能な食品リストをViewに渡します。
     *
     * @param model       画面に渡すデータモデル
     * @param userDetails 認証済みユーザー情報
     * @return テンプレート名 (index)
     */
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        // 1. 今日の食事履歴を取得 (DB修正後のMealLogを使用)
        List<MealLog> todayLogs = foodService.getTodayMealLogs(username);

        // 2. 合計摂取カロリーを計算 (JavaストリームAPIで合算)
        int currentCalories = todayLogs.stream()
                .mapToInt(MealLog::getCalories)
                .sum();

        // 3. 目標カロリーを取得 (BMRサービスから)
        // ※まだプロフィール登録していない場合のハンドリングが必要かも
        int targetCalories = 2200; // 仮の初期値（エラー回避用）
        try {
            targetCalories = bmrService.calculateTargetCalories(username);
        } catch (Exception e) {
            // プロフィール未設定時は仮の値を使い続ける、またはプロフィール画面へ誘導するフラグを立てる等の処理
        }

        // 4. DTOに詰める
        DashboardDto dashboard = new DashboardDto(
                targetCalories,
                currentCalories,
                targetCalories - currentCalories
        );

        // 5. 選択用の食品リストを取得 (マスタデータ)
        List<FoodItem> foodList = foodService.getAvailableFoods(username);

        // Modelに登録してHTMLへ渡す
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("history", todayLogs); // 名前を "foods" から "history" に変更
        model.addAttribute("foodList", foodList); // プルダウン選択用

        return "index";
    }
}