package co.istad.mobilebanking.api.user;

import co.istad.mobilebanking.api.user.web.CreateUserDto;
import co.istad.mobilebanking.api.user.web.UpdateUserDto;
import co.istad.mobilebanking.api.user.web.UserDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserMapStruct userMapStruct;

    @Override
    public UserDto createNewUser(CreateUserDto createUserDto) {
        User user = userMapStruct.createUserDtoToUser(createUserDto);
        log.info("Mapped User: {}", user);
        userMapper.insert(user);
        return this.findUserById(user.getId());
    }

    @Override
    public UserDto findUserById(Integer id) {
        User user = userMapper.selectUserById(id).orElseThrow(() ->
                 new ResponseStatusException(HttpStatus.NOT_FOUND,
                         String.format("User with id = %s not found", id)));
//        userMapper.selectUserById(id).isEmpty();
        return userMapStruct.userToUserDto(user);
    }

    @Override
    public PageInfo<UserDto> findAllUsers(int page, int limit, String name) {
        //                Call repo
        PageInfo<User> userPageInfo = PageHelper.startPage(page, limit)
//                .doSelectPageInfo(userMapper::select);
        .doSelectPageInfo(()-> userMapper.select(name));

        return userMapStruct.userPageInfoToUserDtoPageInfo(userPageInfo);
    }

    @Override
    public Integer deleteUserById(Integer id) {
        boolean isExisted = userMapper.existById(id);
        if (isExisted) {
//            DELETE
            userMapper.deleteById(id);
            return id;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format( "User with id = %s not found", id));
    }

    @Override
    public Integer updateIsDeletedStatusById(Integer id, boolean status) {
        boolean isExisted = userMapper.existById(id);
        if (isExisted) {
            userMapper.updateIsDeleteById(id, status);
            return id;
        }throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("User with %d is not found", id));

    }

    @Override
    public UserDto updateUserById(Integer id, UpdateUserDto updateUserDto) {
       if (userMapper.existById(id)) {
           User user = userMapStruct.updateUserDtoToUser(updateUserDto);
           user.setId(id);
           userMapper.updateById(user);
           return this.findUserById(id);
       }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
        String.format("User with id = %s not found", id));
    }
}
