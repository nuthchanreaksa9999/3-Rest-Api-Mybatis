package co.istad.mobilebanking.api.user.validator.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.*;


@Constraint(validatedBy = {PasswordMatchConstraintValidator.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {

    String message() default "Password is not match!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String password();

    String confirmPassword();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
     @interface List {
        PasswordMatch[] value();
    }

}
