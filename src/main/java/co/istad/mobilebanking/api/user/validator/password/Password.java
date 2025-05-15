package co.istad.mobilebanking.api.user.validator.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {PasswordConstraintValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Password {

    // using with pass say

    String message() default "Your password is not strong enough";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
