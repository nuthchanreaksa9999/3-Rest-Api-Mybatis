package co.istad.mobilebanking.api.user.web;

import co.istad.mobilebanking.api.user.UserService;
import co.istad.mobilebanking.base.BaseRest;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PutMapping("{id}")
    public BaseRest<?> updateUserById(@PathVariable("id") Integer id, @RequestBody UpdateUserDto updateUserDto) {
        UserDto userDto = userService.updateUserById(id, updateUserDto);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("User have been updated successfully.")
                .timestamp(LocalDateTime.now())
                .data(userDto)
                .build();
    }

    @PostMapping("/{id}/is-deleted")
    public BaseRest<?> updateIsDeletedStatusById(@PathVariable Integer id, @RequestBody IsDeletedDto dto) {
        Integer deletedId = userService.updateIsDeletedStatusById(id, dto.status());
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("User have been deleted successfully.")
                .timestamp(LocalDateTime.now())
                .data(deletedId)
                .build();
    }

    @DeleteMapping("/{id}")
    public BaseRest<?> deleteUserById(@PathVariable Integer id) {
       Integer deletedId = userService.deleteUserById(id);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("User have been deleted successfully.")
                .timestamp(LocalDateTime.now())
                .data(deletedId)
                .build();
    }

    @GetMapping("/{identifier}")
    public BaseRest<?> findUserById(@PathVariable("identifier") String identifier) {
        UserDto userDto;
        /*UserDto userDto = userService.findUserById(id);*/

        try {
            Integer id = Integer.parseInt(identifier);
            userDto = userService.findUserById(id);
        }catch (NumberFormatException e) {
            userDto = userService.findUserByStudentCardId(identifier);
        }

        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("User have been found successfully.")
                .timestamp(LocalDateTime.now())
                .data(userDto)
                .build();
    }

/*    @GetMapping("/{student-card-id}")
    public BaseRest<?> findUserByStudentCardById(@PathVariable("student-card-id") String studentCardId) {
        log.info("studentCardId: {}", studentCardId);
        return null;
    }*/

    @GetMapping
    public BaseRest<?> findAllUsers(@RequestParam(name = "page", required = false, defaultValue = "1") int page
    ,                                @RequestParam(name = "limit", required = false, defaultValue = "20") int limit,
                                    @RequestParam(value = "name", required = false, defaultValue = "") String name
                                    ) {
        PageInfo<UserDto> userDtoPageInfo = userService.findAllUsers(page, limit, name);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("User have been found successfully.")
                .timestamp(LocalDateTime.now())
                .data(userDtoPageInfo)

                .build();
    }

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
