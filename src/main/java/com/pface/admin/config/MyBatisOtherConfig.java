package com.pface.admin.config;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
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
@MapperScan(basePackages = "com.pface.admin.otherdb.mapper",sqlSessionFactoryRef = "otherSqlSessionFactory")
public class MyBatisOtherConfig {

    //https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/en/HowToUse.md
    //https://pagehelper.github.io/docs/howtouse/
    @Bean(name = "otherDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.other")
    public DataSource dataSource(DataSourceProperties properties) {
       return DataSourceBuilder.create().build();
       /* return DataSourceBuilder.create(properties.getClassLoader())
                .type(HikariDataSource.class)
                .driverClassName(properties.determineDriverClassName())
                .url(properties.determineUrl())
                .username(properties.determineUsername())
                .password(properties.determinePassword())
                .build();
      */
    }

    @Bean(name = "otherTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("otherDataSource") DataSource dataSource)
    { return new DataSourceTransactionManager(dataSource); }


    @Bean(name = "otherSqlSessionFactory")
    public SqlSessionFactory basicSqlSessionFactory(@Qualifier("otherDataSource") DataSource basicDataSource) throws Exception
    {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(basicDataSource);
        factoryBean.setMapperLocations( new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/other/*.xml"));

        //分页插件
        //https://www.cnblogs.com/jpfss/p/9723828.html
        Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "sqlserver");//sqlserver mysql
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments","true");
        properties.setProperty("autoRuntimeDialect","true");
        properties.setProperty("params","pageNum=pageNumKey;pageSize=pageSizeKey;");
        interceptor.setProperties(properties);
        factoryBean.setPlugins(new Interceptor[]{interceptor});
        return factoryBean.getObject();
    }

    @Bean("otherMapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("otherSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.pface.admin.otherdb.mapper");

        Properties properties1 = new Properties();
        //properties1.setProperty("mappers", "ccom.pface.admin.core.utils.MyMapper");
        properties1.setProperty("notEmpty", "false");
        properties1.setProperty("IDENTITY", "sqlserver");
        properties1.setProperty("style", "normal");
        Properties properties = properties1;
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

    @Bean(name = "otherSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("otherSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception
    {
        return new SqlSessionTemplate(sqlSessionFactory);
    }





}
