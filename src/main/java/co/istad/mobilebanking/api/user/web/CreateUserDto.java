package co.istad.mobilebanking.api.user.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(

        @NotBlank String name,

        @NotBlank String gender,

        String oneSignalId,

        String studentCardId,

        @NotNull Boolean isStudent

) {
}
