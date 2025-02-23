package com.healthcare.booking.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class Condition {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/healthcare_booking");
        dataSource.setUsername("namvn");
        dataSource.setPassword("15012001");

        return dataSource;
    }
}
