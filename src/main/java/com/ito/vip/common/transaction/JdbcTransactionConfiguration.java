package com.ito.vip.common.transaction;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

public class JdbcTransactionConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager getJdbcTransactionManager() {
        //this.bindDataSourceProperty();
        return new DataSourceTransactionManager(dataSource);
    }
}
