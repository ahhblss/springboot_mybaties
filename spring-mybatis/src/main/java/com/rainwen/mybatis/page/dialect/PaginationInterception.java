package com.rainwen.mybatis.page.dialect;

import com.rainwen.mybatis.page.PageRequest;
import com.rainwen.mybatis.page.Pageable;
import com.rainwen.mybatis.page.util.PageUtils;
import com.rainwen.mybatis.page.util.ReflectUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;
import java.util.Properties;

/**
 * 拦截器实现分页
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class})})
public class PaginationInterception implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
        BoundSql boundSql = delegate.getBoundSql();
        PageUtils.Param pageMap = PageUtils.getPageMap(boundSql);
        if (pageMap != null) {
            Pageable pageable = (Pageable) pageMap.getEntry().getValue();
            Configuration configuration = (Configuration) ReflectUtil.getFieldValue(delegate, "configuration");
            MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
            Connection connection = (Connection) invocation.getArgs()[0];
            totalCount(connection, boundSql, configuration, mappedStatement, pageable);
            ReflectUtil.setFieldValue(boundSql, "sql",
                    searchDialectByDbTypeEnum(configuration, pageable).spellPageSql(boundSql.getSql(), pageable.getOffset(), pageable.getPageSize()));
        }
        return invocation.proceed();
    }

    /**
     * 获取总页数
     *
     * @param conn
     * @param orgBoundSql
     * @param configuration
     * @param mappedStatement
     * @return
     * @throws java.sql.SQLException
     */
    private void totalCount(Connection conn, BoundSql orgBoundSql,
                            Configuration configuration, MappedStatement mappedStatement, Pageable pageable) throws SQLException {
        int totalCount = 0;
        String countSpellSql = searchDialectByDbTypeEnum(configuration, pageable).getCountSql(orgBoundSql.getSql());
        PreparedStatement preparedStatement = conn.prepareStatement(countSpellSql);
        Object parameterObject = orgBoundSql.getParameterObject();
        BoundSql boundSql = new BoundSql(configuration, countSpellSql,
                orgBoundSql.getParameterMappings(),
                parameterObject);
        setParameters(preparedStatement, mappedStatement, boundSql, parameterObject);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs != null) {
            if (rs.next()) {
                totalCount = rs.getInt(1);
                PageRequest pageRequest = (PageRequest) pageable;
                pageRequest.setTotalElements(totalCount);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }


    /**
     * 获取方言对象
     *
     * @param pageable
     * @return
     */
    private Dialect searchDialectByDbTypeEnum(Configuration configuration, Pageable pageable) {
        Dialect dialect = null;
        switch (searchDbTypeByConfig(configuration)) {
            case MYSQL:
                dialect = new MysqlDialect(pageable.getSort());
                break;
            case SQLSERVER:
                break;
            case DB2: //TODO
                break;
            default:
                dialect = new OracleDialect(pageable.getSort());
                break;
        }
        return dialect;
    }

    /**
     * 获取数据库类型
     *
     * @return 返回数据库类型的枚举对象
     */
    private Dialect.Type searchDbTypeByConfig(Configuration configuration) {
        String dialectConfig = configuration.getVariables().getProperty("sqlType");
        if (dialectConfig != null && !"".equals(dialectConfig)) {
            return Dialect.Type.valueOf(dialectConfig.toUpperCase());
        } else {
            throw new RuntimeException(
                    "databaseType is null , please check your mybatis configuration!");
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

}