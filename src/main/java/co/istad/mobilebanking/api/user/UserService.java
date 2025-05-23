package co.istad.mobilebanking.api.user;


import co.istad.mobilebanking.api.user.web.CreateUserDto;
import co.istad.mobilebanking.api.user.web.UpdateUserDto;
import co.istad.mobilebanking.api.user.web.UserDto;
import com.github.pagehelper.PageInfo;

public interface UserService {

    UserDto createNewUser(CreateUserDto createUserDto);

     UserDto findUserById(Integer id);

     UserDto findUserByStudentCardId(String studentCardId);

     PageInfo<UserDto> findAllUsers(int page, int limit, String name);

     Integer deleteUserById(Integer id);

     Integer updateIsDeletedStatusById(Integer id, boolean status);

     UserDto updateUserById(Integer id, UpdateUserDto updateUserDto);

}
