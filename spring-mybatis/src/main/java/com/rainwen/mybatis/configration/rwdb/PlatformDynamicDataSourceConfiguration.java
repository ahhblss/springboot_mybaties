package com.rainwen.mybatis.configration.rwdb;

import com.rainwen.mybatis.page.dialect.PaginationInterception;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 动态数据源注入MyBatis、事务配置
 * Created by rocky on 14-5-29.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class PlatformDynamicDataSourceConfiguration {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dataSource) throws SQLException, IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        Properties properties = new Properties();
        properties.setProperty("sqlType", "mysql");
        sqlSessionFactoryBean.setConfigurationProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PaginationInterception()});
        return sqlSessionFactoryBean;
    }

    @Bean
    public DynamicDataSourceTransactionManager transactionManager(@Qualifier("dynamicDataSource") DynamicDataSource dataSource) {
        DynamicDataSourceTransactionManager dynamicDataSourceTransactionManager = new DynamicDataSourceTransactionManager();
        dynamicDataSourceTransactionManager.setDataSource(dataSource);
        return dynamicDataSourceTransactionManager;
    }


}