package com.whopuppy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

	@Bean
	public DataSourceTransactionManager mybatisTransactionManager(DataSource dataSource) {
	  return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(DataSourceTransactionManager mybatisTransactionManager
		, JpaTransactionManager jpaTransactionManager) throws Exception {
		ChainedTransactionManager transactionManager = new ChainedTransactionManager(jpaTransactionManager, mybatisTransactionManager);
		return transactionManager;
	}
}
