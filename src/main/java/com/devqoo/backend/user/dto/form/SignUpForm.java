package com.devqoo.backend.user.dto.form;

import com.devqoo.backend.user.validation.PasswordMatches;
import com.devqoo.backend.user.validation.PasswordPolicy;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record SignUpForm(

    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(max = 50, message = "이메일은 50자 이하로 입력해주세요.")
    @Email(message = "형식을 확인하세요.")
    String email,

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 20, message = "닉네임은 20자 이하로 입력해주세요.")
    String nickName,

    @PasswordPolicy
    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password,

    String passwordConfirm
) {

}
