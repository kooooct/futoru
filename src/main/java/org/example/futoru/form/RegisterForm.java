package org.example.futoru.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterForm {

    @NotBlank(message = "ユーザIDを入力してください")
    @Size(min = 3, max = 20, message = "ユーザIDは3文字以上20文字以内で入力してください")
    private String username;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 4, message = "パスワードは4文字以上で入力してください")
    private String password;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmPassword;
}