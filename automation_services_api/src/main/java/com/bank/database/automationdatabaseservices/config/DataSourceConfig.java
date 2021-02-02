package com.bank.database.automationdatabaseservices.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean(name = "dsCustom")
    public DataSource dataSource(){
        return DataSourceBuilder.create()
                .username(Config.DBUSERNAME)
                .password(Config.DBPASSWORD)
                .url("jdbc:mysql://"+Config.DBHOST+":"+Config.DBPORT+"/"+Config.DBNAME+"?useSSL=false")
                .build();
    }

    @Bean(name = "jdbcCustom")
    @Autowired
    public JdbcTemplate jdbcTemplate(@Qualifier("dsCustom") DataSource dsCustom){
        return new JdbcTemplate(dsCustom);
    }
}
