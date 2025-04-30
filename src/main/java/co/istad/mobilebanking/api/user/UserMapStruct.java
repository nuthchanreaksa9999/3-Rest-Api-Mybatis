package co.istad.mobilebanking.api.user;

import co.istad.mobilebanking.api.user.web.CreateUserDto;
import co.istad.mobilebanking.api.user.web.UpdateUserDto;
import co.istad.mobilebanking.api.user.web.UserDto;
import com.github.pagehelper.PageInfo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapStruct {

    User updateUserDtoToUser(UpdateUserDto updateUserDto);

    User createUserDtoToUser(CreateUserDto createUserDto);

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    PageInfo<UserDto> userPageInfoToUserDtoPageInfo(PageInfo<User> userPageInfo);
}
