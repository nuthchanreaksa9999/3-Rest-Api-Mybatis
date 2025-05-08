package co.istad.mobilebanking.api.auth;

import org.apache.ibatis.jdbc.SQL;

public class AuthProvider {

    public String buildRegisterSql() {

        return new SQL(){{
            INSERT_INTO("users");
            VALUES("email", "#{u.email}");
            VALUES("password", "#{u.password}");
            VALUES("is_verified", "#{u.isVerified}");
            VALUES("is_deleted", "FALSE");
        }}.toString();

    }

    public String buildSelectedByEmailAndVerifiedCodeSql() {

        return new SQL(){{
            SELECT("*");
            FROM("users");
            WHERE("email = #{email}",
                            "verified_code = #{verifiedCode}");
        }}.toString();

    }

    public String buildVerifiedSql() {
        return new SQL(){{

            UPDATE("users");
            SET("is_verified = TRUE");
            SET("verified_code = NULL");
            WHERE("email = #{email}", "verified_code = #{verifiedCode}");

        }}.toString();
    }

    public String buildVerifiedCodeSql(){

        return new SQL(){{

            UPDATE("users");
            SET("verified_code = #{verifiedCode}");
            WHERE("email = #{email}");

        }}.toString();

    }

}
