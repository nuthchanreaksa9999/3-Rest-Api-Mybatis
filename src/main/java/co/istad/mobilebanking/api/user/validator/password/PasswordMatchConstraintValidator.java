package co.istad.mobilebanking.api.user.validator.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchConstraintValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String password;

    private String confirmPassword;

    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.confirmPassword = constraintAnnotation.confirmPassword();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(value).getPropertyValue(confirmPassword);

        boolean isValid = false;

        if (passwordValue != null) {
            isValid  = passwordValue.equals(confirmPasswordValue);
        }

        if (!isValid) {

            //Sending one message each time failed validation
            context.disableDefaultConstraintViolation();

            // build message for password property
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(password)
                    .addConstraintViolation();

            // build message for confirm password property
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(confirmPassword)
                    .addConstraintViolation();

        }

        return isValid;
    }

}
