package com.rainwen.mybatis.page.dialect;

public class OracleDialect extends Dialect {
    private String orderBy;


    public OracleDialect(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String spellPageSql(String sql, int offset, int limit) {
        sql = sql.trim();
        StringBuffer buffer = new StringBuffer(sql.length() + 100);
        buffer.append(" SELECT * FROM (")
                .append(" SELECT ROW_.* , ROWNUM AS ROWNUM_ FROM (")
                .append(sql)
                .append(" ORDER BY ")
                .append(orderBy)
                .append(") ROW_) WHERE ROWNUM_>")
                .append(offset)
                .append(" and ROWNUM_ <= ")
                .append(limit);
        return buffer.toString();
    }
}
