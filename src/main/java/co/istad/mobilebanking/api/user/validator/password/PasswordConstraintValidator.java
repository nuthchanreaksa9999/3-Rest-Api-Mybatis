package co.istad.mobilebanking.api.user.validator.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.passay.*;
import java.util.Arrays;

@RequiredArgsConstructor
public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        PasswordValidator passwordValidator = new PasswordValidator(

                Arrays.asList(

                        // Length rule. Min 10 mx 128 characters
                        new LengthRule(6, 15),

                        // At least one upper case letter
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),

                        // At least one lower case letter
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),

                        // at least one number
                        new CharacterRule(EnglishCharacterData.Digit, 1),

                        // At least one number
                        new CharacterRule(EnglishCharacterData.Special, 1),

                        new WhitespaceRule()

                )

        );

        // validation follow password pattern
        RuleResult result = passwordValidator.validate(new PasswordData(password));

        if (result.isValid())
            return true;


        // Sending one message each time failed validator.
        context.buildConstraintViolationWithTemplate(passwordValidator.getMessages(result).stream().findFirst().get())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;

    }
}
