package co.istad.mobilebanking.api.auth.web;

import co.istad.mobilebanking.api.user.validator.email.EmailUnique;
import co.istad.mobilebanking.api.user.validator.password.Password;
import co.istad.mobilebanking.api.user.validator.password.PasswordMatch;
import co.istad.mobilebanking.api.user.validator.role.RoleIdConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@PasswordMatch(password = "password", confirmPassword = "confirmPassword")
public record RegisterDto(

        @NotBlank (message = "Email is required!")
        @EmailUnique
        @Email
        String email,

        @NotBlank (message = "Password is required")
        @Password
        String password,

        @NotBlank(message = "Confirmed password is required")
        @Password
        String confirmPassword,

        @NotNull(message = "Roles are required")
        @RoleIdConstraint
        List<Integer> roleIds

) {
}
