package org.example.futoru.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * ユーザー認証情報および身体データを管理するエンティティクラス。
 * Spring SecurityのUserDetailsを実装し、認可処理に必要な権限情報も保持する。
 */
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    /** 権限ロール (例: "USER", "ADMIN") */
    private String role;

    /** 年齢 */
    private Integer age;

    /** 性別 ("MALE" または "FEMALE") */
    private String gender;

    /** 身長 (cm) */
    private Double height;

    /** 体重 (kg) */
    private Double weight;

    /** 活動レベル ("LOW", "MID", "HIGH") */
    private String activityLevel;

    /** 目標カロリー (手動設定用、未設定時は自動計算を使用) */
    private Integer targetCalories;

    /**
     * ユーザーに付与された権限を返却する。
     * roleフィールドが設定されている場合、SimpleGrantedAuthorityとして返却する。
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}