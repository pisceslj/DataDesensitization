package com.data.desensitization.Configuration.databases;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TestDataSourceConfig {
	/*
	 * Object of dataSource configuration
	 *  Primary stands for default Object 
	 * @return
	 */
	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource.test")
	public DataSourceProperties testDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	/* 
	 * DataSource Object
	 */
	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource.test")
	public DataSource testDataSource() {
		return testDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	@Primary
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(testDataSource());
	}
}
