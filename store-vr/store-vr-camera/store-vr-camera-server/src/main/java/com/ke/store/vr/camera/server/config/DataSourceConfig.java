package com.ke.store.vr.camera.server.config;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据源的一些配置，主要是配置读写分离的sqlsession，这里没有使用mybatis annotation
 *
 * @author Jail Hu
 * @copyright (c) 2018, Lianjia Group All Rights Reserved.
 */
@Configuration
@EnableTransactionManagement
@SuppressWarnings("deprecation")
@MapperScan(basePackages = {"com.ke.store.vr.camera.server.dao"}, sqlSessionTemplateRef = "sqlsessionCommon")
/* non-public */ class DataSourceConfig {
  /** 可读写的SQL Session */
  public static final String BEANNAME_SQLSESSION_COMMON = "sqlsessionCommon";

  /** 事务管理器的名称，如果有多个事务管理器时，需要指定beanName */
  public static final String BEANNAME_TRANSACTION_MANAGER = "transactionManager";

  /** 主数据源，必须配置，spring启动时会执行初始化数据操作（无论是否真的需要），选择查找DataSource class类型的数据源 配置通用数据源，可读写，连接的是主库 */
  @Bean
  @Primary
  @ConfigurationProperties(prefix = "datasource.common")
  public DataSource datasourceCommon() {
    return DataSourceBuilder.create().build();
  }


  /**
   * 配置事务管理器，建议使用手动的{@code Transactional}，而不是自动的aop切面
   */
  @Bean(name = BEANNAME_TRANSACTION_MANAGER)
  public DataSourceTransactionManager createDataSourceTransactionManager() {
    DataSource dataSource = this.datasourceCommon();
    DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
    return manager;
  }

  /**
   * 配置读写sqlsession
   */
  @Bean(name = BEANNAME_SQLSESSION_COMMON)
  public SqlSession readWriteSqlSession() throws Exception {
    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    factory.setDataSource(this.datasourceCommon());
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	  factory.setConfigLocation(resolver.getResources("classpath*:mybatis/mybatis-config.xml")[0]);
	  factory.setMapperLocations(resolver.getResources("classpath*:mybatis/mappers/*.xml"));
    return new SqlSessionTemplate(factory.getObject());
  }


}
