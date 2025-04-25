package co.istad.mobilebanking.api.user;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper; // ✅ correct


@Mapper
public interface UserMapper {

    @InsertProvider(type = UserProvider.class, method = ("buildInsertSql"))
    void insert(@Param("u") User user);
}
