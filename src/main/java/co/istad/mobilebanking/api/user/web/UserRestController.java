package co.istad.mobilebanking.api.user.web;

import co.istad.mobilebanking.api.user.UserService;
import co.istad.mobilebanking.base.BaseRest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;



    @PostMapping
    public BaseRest<?> createNewUser(@RequestBody @Valid CreateUserDto createUserDto)
    {
       UserDto userDto = userService.createNewUser(createUserDto);

            return BaseRest.builder()
            .status(true)
            .code(HttpStatus.OK.value())
            .message("User have been created successfully.")
                    .timestamp(LocalDateTime.now())
            .data(userDto)

            .build();
    }
}
