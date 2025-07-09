package com.devqoo.backend.user.dto.form;

import com.devqoo.backend.user.validation.PasswordConfirmable;
import com.devqoo.backend.user.validation.PasswordMatches;
import com.devqoo.backend.user.validation.PasswordPolicy;
import jakarta.validation.constraints.NotBlank;

@PasswordMatches
public record PasswordUpdateForm(

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String originPassword,

    @PasswordPolicy
    @NotBlank(message = "변경할 비밀번호를 입력해주세요.")
    String password,
    String passwordConfirm
) implements PasswordConfirmable {

}
