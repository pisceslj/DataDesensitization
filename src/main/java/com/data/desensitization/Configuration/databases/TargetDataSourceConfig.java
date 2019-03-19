package com.data.desensitization.Configuration.databases;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class TargetDataSourceConfig {
	@Bean
	@ConfigurationProperties("spring.datasource.target")
	public DataSourceProperties targetDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource.target")
	public DataSource targetDataSource() {
		return targetDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	@Bean(name = "jdbcTemplate2")
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(targetDataSource());
	}
}
