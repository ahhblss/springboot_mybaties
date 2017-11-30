package com.rainwen.mybatis.configration.singledb;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by serv on 2014/10/11.
 */
//@Configuration
//@EnableConfigurationProperties(DruidSettings.class)
public class DruidConfiguration {

    @Primary
    @Bean(name = "writeDataSource", destroyMethod = "close" , initMethod = "init")
    public DataSource dataSource(DruidSettings ds) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource(false);
        if(ds.getId()!=null){
            druidDataSource.setName(ds.getId());
        }
        druidDataSource.setUsername(ds.getUsername());
        druidDataSource.setUrl(ds.getUrl());
        druidDataSource.setPassword(ds.getPassword());
        druidDataSource.setFilters(ds.getFilters());
        druidDataSource.setMaxActive(ds.getMaxActive());
        druidDataSource.setInitialSize(ds.getInitialSize());
        druidDataSource.setMaxWait(ds.getMaxWait());
        druidDataSource.setMinIdle(ds.getMinIdle());
        druidDataSource.setTimeBetweenEvictionRunsMillis(ds.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(ds.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(ds.getValidationQuery());
        druidDataSource.setTestWhileIdle(ds.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(ds.isTestOnBorrow());
        druidDataSource.setTestOnReturn(ds.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(ds.isPoolPreparedStatements());
        druidDataSource.setMaxOpenPreparedStatements(ds.getMaxOpenPreparedStatements());
        return druidDataSource;
    }


    @Bean(name = "stat-filter")
    public StatFilter statFilter(DruidSettings ds) {
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(ds.getSlowSqlMillis());
        statFilter.setLogSlowSql(ds.isLogSlowSql());
        return statFilter;
    }

}
