package co.istad.mobilebanking.api.user;


import co.istad.mobilebanking.api.user.web.CreateUserDto;
import co.istad.mobilebanking.api.user.web.UserDto;

public interface UserService {

    UserDto createNewUser(CreateUserDto createUserDto);

}
