package co.istad.mobilebanking.api.accountType;

import org.apache.ibatis.jdbc.SQL;

public class AccountTypeProvider {

    private static final String tableName = "account_types";


    public String buildSelectSql(){
        return new SQL(){{
//            TODO:
            SELECT("*");
            FROM("account_types");

        }}.toString();
    }

    public String buildSelectByIdSql(){
        return new SQL(){{
            SELECT("*");
            FROM("account_types");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String buildInsertSql(){
        return new SQL(){{
            INSERT_INTO(tableName);
            VALUES("name", "#{ac.name}");
        }}.toString();
    }

    public String buildUpdateSql(){
        return new SQL(){{
            UPDATE(tableName);
            SET("name = #{ac.name}");
            WHERE("id = #{ac.id}");
        }}.toString();
    }

}
