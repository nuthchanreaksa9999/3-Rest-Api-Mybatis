package co.istad.mobilebanking.api.user;

import org.apache.ibatis.annotations.*;

import java.util.Optional;


@Mapper
public interface UserMapper {

    @InsertProvider(type = UserProvider.class, method = ("buildInsertSql"))
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(@Param("u") User user);

    @SelectProvider(type = UserProvider.class, method = "buildSelectByIdSql")
    @Result(column = "student_card_id", property = "studentCardId")
    @Result(column = "is_student", property = "isStudent")
    Optional<User> selectUserById(@Param("id") Integer id);

    @Select("SELECT EXISTS(SELECT * FROM users WHERE id = #{id})")
    boolean existById(@Param("id") Integer id);

    @DeleteProvider(type = UserProvider.class, method = "buildDeleteByIdSql")
    void deleteById(@Param("id") Integer id);

    @UpdateProvider(type = UserProvider.class, method = "buildUpdateIsDeletedByIdSql")
    void updateIsDeleteById(@Param("id") Integer id, @Param("status") boolean status);

}
