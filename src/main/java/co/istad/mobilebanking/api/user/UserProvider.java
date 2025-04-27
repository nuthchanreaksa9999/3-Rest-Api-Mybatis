package co.istad.mobilebanking.api.user;


import org.apache.ibatis.jdbc.SQL;

public class UserProvider {

    private static final String tableName = "users";
    public String buildInsertSql(){

    return new SQL(){{
        INSERT_INTO(tableName);
        VALUES("name", "#{u.name, javaType=String, jdbcType=VARCHAR}");
        VALUES("gender", "#{u.gender, javaType=String, jdbcType=VARCHAR}");
        VALUES("one_signal_id", "#{u.oneSignalId, javaType=String, jdbcType=VARCHAR}");
        VALUES("is_deleted","FALSE");
        VALUES("is_student", "#{u.isStudent, javaType=Boolean, jdbcType=BOOLEAN}");
        VALUES("student_card_id", "#{u.studentCardId, javaType=String, jdbcType=VARCHAR}");

//        VALUES("is_deleted","FALSE");
    }}.toString();

    };

}
