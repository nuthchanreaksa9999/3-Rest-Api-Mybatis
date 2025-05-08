package co.istad.mobilebanking.api.auth.web;

import jakarta.validation.constraints.NotBlank;

public record RegisterDto(

        @NotBlank (message = "Email is required")
        String email,

        @NotBlank (message = "Password is required")
        String password,

        @NotBlank(message = "Confirmed password is required")
        String confirmPassword

) {
}
