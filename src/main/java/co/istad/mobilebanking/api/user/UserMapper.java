package co.istad.mobilebanking.api.user;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;


@Mapper
public interface UserMapper {

    @InsertProvider(type = UserProvider.class, method = ("buildInsertSql"))
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(@Param("u") User user);

    @SelectProvider(type = UserProvider.class, method = "buildSelectByIdSql")
    @Results (id = "userResultMap", value = {
            @Result(column = "student_card_id", property = "studentCardId"),
            @Result(column = "is_student", property = "isStudent")
    })
    Optional<User> selectUserById(@Param("id") Integer id);

    @ResultMap("userResultMap")
    @SelectProvider(type = UserProvider.class, method = "buildSelectSql")
    List<User> select(@Param("name") String name);

    @Select("SELECT EXISTS(SELECT * FROM users WHERE id = #{id})")
    boolean existById(@Param("id") Integer id);

    @DeleteProvider(type = UserProvider.class, method = "buildDeleteByIdSql")
    void deleteById(@Param("id") Integer id);

    @UpdateProvider(type = UserProvider.class, method = "buildUpdateIsDeletedByIdSql")
    void updateIsDeleteById(@Param("id") Integer id, @Param("status") boolean status);

    @UpdateProvider(type = UserProvider.class, method = "buildUpdateByIdSql")
    void updateById(@Param("u") User user);

}
