package com.pface.admin.config;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.pface.admin.modules.*.mapper",sqlSessionFactoryRef = "masterSqlSessionFactory")
//@MapperScan(basePackages = "com.pface.admin.modules.*.mapper")
public class MyBatisConfig {

    //https://www.cnblogs.com/Jacck/p/8309421.html

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource dataSource(DataSourceProperties properties) {
        return DataSourceBuilder.create().build();
       /* return DataSourceBuilder.create(properties.getClassLoader())
                .type(HikariDataSource.class)
                .driverClassName(properties.determineDriverClassName())
                .url(properties.determineUrl())
                .username(properties.determineUsername())
                .password(properties.determinePassword())
                .build();*/
    }
    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception
    {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage("com.pface.admin.modules.*.po");
        //factoryBean.set
        factoryBean.setMapperLocations( new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));

        //分页插件
        Interceptor interceptor = new PageInterceptor();
        //PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments","true");
        properties.setProperty("autoRuntimeDialect","true");
        properties.setProperty("params","pageNum=pageNumKey;pageSize=pageSizeKey;");
        interceptor.setProperties(properties);
        //factoryBean.setPlugins(new Interceptor[] {pageHelper});
        //添加插件
        factoryBean.setPlugins(new Interceptor[]{interceptor});


        return factoryBean.getObject();
    }

    @Bean("masterMapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("masterSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.pface.admin.modules.*.mapper");
        Properties properties1 = new Properties();
        //properties1.setProperty("mappers", "ccom.pface.admin.core.utils.MyMapper");
        properties1.setProperty("notEmpty", "false");
        properties1.setProperty("IDENTITY", "MYSQL");
        properties1.setProperty("style", "normal");
        Properties properties = properties1;
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

    @Primary
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("masterDataSource") DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception
    {
        return new SqlSessionTemplate(sqlSessionFactory);
    }





}
