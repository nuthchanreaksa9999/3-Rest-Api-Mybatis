package co.istad.mobilebanking.api.auth;

import co.istad.mobilebanking.api.auth.web.AuthDto;
import co.istad.mobilebanking.api.auth.web.LogInDto;
import co.istad.mobilebanking.api.auth.web.RegisterDto;
import co.istad.mobilebanking.api.auth.web.TokenDto;

public interface AuthService {

    AuthDto refreshToken(TokenDto tokenDto);

    AuthDto login(LogInDto logInDto);

    void register(RegisterDto registerDto);

    void verify(String email);

    void checkVerify(String email, String verifiedCode);

}
