package com.rainwen.mybatis.configration.singledb;

import com.rainwen.mybatis.page.dialect.PaginationInterception;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by rocky on 14-5-29.
 */
//@Configuration
//@EnableTransactionManagement(proxyTargetClass = true)
//@Import({DruidConfiguration.class, DruidWebConfiguration.class})
public class PlatformConfiguration {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws SQLException, IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        Properties properties = new Properties();
        properties.setProperty("sqlType", "mysql");
        sqlSessionFactoryBean.setConfigurationProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PaginationInterception()});
        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


}