package com.rainwen.mybatis.page.dialect;

/**
 * Created by rain.wen on 2017/7/26.
 */
public abstract class Dialect {

    public static enum Type {
        MYSQL,
        ORACLE,
        SQLSERVER,
        DB2
    }

    public abstract String spellPageSql(String sql, int offset, int limit);

    public String getCountSql(String sql) {
        return "SELECT COUNT(0) FROM (" + sql + ") TEMP";
    }


}
