package co.istad.mobilebanking.api.auth.web;

import co.istad.mobilebanking.api.user.validator.EmailUnique;
import co.istad.mobilebanking.api.user.validator.RoleIdConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegisterDto(

        @NotBlank (message = "Email is required!")
        @EmailUnique
                @Email
        String email,

        @NotBlank (message = "Password is required")
        String password,

        @NotBlank(message = "Confirmed password is required")
        String confirmPassword,

        @NotNull(message = "Roles are required")
                @RoleIdConstraint
        List<Integer> roleIds

) {
}
