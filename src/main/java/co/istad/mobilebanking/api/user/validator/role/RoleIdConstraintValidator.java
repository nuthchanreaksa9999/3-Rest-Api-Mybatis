package co.istad.mobilebanking.api.user.validator.role;

import co.istad.mobilebanking.api.user.UserMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoleIdConstraintValidator implements ConstraintValidator<RoleIdConstraint, List<Integer> > {

    private final UserMapper userMapper;

    @Override
    public boolean isValid(List<Integer> roleIds, ConstraintValidatorContext constraintValidatorContext) {

        for (Integer roleId : roleIds) {

            if (!userMapper.checkRoleId(roleId)){
                return false;
            }
        }

        return true;
    }
}
