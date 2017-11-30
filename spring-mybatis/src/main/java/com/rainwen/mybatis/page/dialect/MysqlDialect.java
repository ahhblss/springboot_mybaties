package com.rainwen.mybatis.page.dialect;

/**
 * Created by rocky on 14-6-4.
 */
public class MysqlDialect extends Dialect {

    private String orderBy;

    /**
     * 构造方法
     *
     * @param orderBy 类似 column1 desc , column2 asc
     */
    public MysqlDialect(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String spellPageSql(String sql, int offset, int limit) {
        sql = sql.trim();
        StringBuffer buffer = new StringBuffer(sql.length() + 100);
        buffer.append(" SELECT ROW_.* FROM (").append(sql).append(") ROW_");
        if (orderBy!=null&&!"".equals(orderBy)) {
            buffer.append(" ORDER BY ").append(orderBy);
        }
        buffer.append(" limit ").append(offset).append(",").append(limit);
        return buffer.toString();
    }
}
