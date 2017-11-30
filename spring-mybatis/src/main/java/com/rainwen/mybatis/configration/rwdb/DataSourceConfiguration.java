package com.rainwen.mybatis.configration.rwdb;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多数据源配置，此方式省去配置文件，但灵活性不够
 * Created by rain.wen on 2017/7/31.
 */
//@Configuration
public class DataSourceConfiguration {

    @Bean(name = "masterDataSource", destroyMethod = "close", initMethod = "init")
    @Primary // 这个注解表示主数据源
    @ConfigurationProperties(prefix = "druid.datasource")
    public DataSource datasource() {
        return new DruidDataSource();
    }

    @Bean(name = "slaveDataSource", destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "druid.datasource2")
    public DataSource datasource2() {
        return new DruidDataSource();
    }

    /**
     * 构建动态数据源
     * @param masterDataSource
     * @param slaveDataSource
     * @return
     */
    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                               @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

        targetDataSources.put("master", masterDataSource);
        targetDataSources.put("slave1", slaveDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);

        List<Object> slaveDataSources = new ArrayList<Object>();
        slaveDataSources.add("slave1");

        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        dynamicDataSource.setSlaveDataSources(slaveDataSources);

        return dynamicDataSource;

    }

}
