package co.istad.mobilebanking.api.auth;

import co.istad.mobilebanking.api.auth.web.RegisterDto;

public interface AuthService {

    void register(RegisterDto registerDto);

    void verify(String email);

    void checkVerify(String email, String verifiedCode);

}
