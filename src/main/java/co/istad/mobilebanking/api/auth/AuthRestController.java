package co.istad.mobilebanking.api.auth;

import co.istad.mobilebanking.api.auth.web.RegisterDto;
import co.istad.mobilebanking.base.BaseRest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/register")
    public BaseRest<?> register(@Valid @RequestBody RegisterDto registerDto) {

        // call service
        authService.register(registerDto);

        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("Registered Successfully")
                .timestamp(LocalDateTime.now())
                .data(registerDto.email())
                .build();
    }

    @PostMapping("/verify")
    public BaseRest<?> verify(@RequestParam String email) {

        authService.verify(email);

        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("Please check your Email and Verify!!")
                .timestamp(LocalDateTime.now())
                .data(email)
                .build();

    }

    @GetMapping("/check-verify")
    public BaseRest<?> checkVerify(@RequestParam String email,
                                   @RequestParam String verifiedCode
    ) {
        log.info("email: {}", email);
        log.info("Verified Code: {}", verifiedCode);

        authService.checkVerify(email, verifiedCode);

        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("You have been verified successfully")
                .timestamp(LocalDateTime.now())
                .data(email)
                .build();

    }
}
