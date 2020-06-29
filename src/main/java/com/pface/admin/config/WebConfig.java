package com.pface.admin.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pface.admin.core.filter.FrontLoginInterceptor;
import com.pface.admin.core.filter.JiekouIntercepter;
import com.pface.admin.modules.base.bean.ImageFileBean;
import com.pface.admin.modules.base.enums.BaseEnumDeserializer;
import com.pface.admin.modules.base.enums.EnumConvertFactory;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter  implements EnvironmentAware {

    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Autowired
    private FrontLoginInterceptor frontLoginInterceptor;

    @Autowired
    private JiekouIntercepter jiekouIntercepter;

    @Autowired
    private ImageFileBean imageFileBean;


    String pubfile;


    public WebConfig() {}


    @PostConstruct
    public void initThreadContext() {
        // 使其在异步线程中也能正常使用RequestContext
        dispatcherServlet.setThreadContextInheritable(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new LogInterceptor()).addPathPatterns();
       registry.addInterceptor(frontLoginInterceptor).addPathPatterns("/front/**")
               .excludePathPatterns("/front/login")
               .excludePathPatterns("/front/reg")
               .excludePathPatterns("/front/user/login")
               .excludePathPatterns("/front/user/sendSmsCode")
               .excludePathPatterns("/front/user/regUser");

        registry.addInterceptor(jiekouIntercepter)
                .addPathPatterns("/appapi/**")
                .excludePathPatterns("/appapi/content/list");
       super.addInterceptors(registry);

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("pubfile="+pubfile);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/pubfile/**").addResourceLocations("file:"+imageFileBean.getPublicPath());
     //  registry.addResourceHandler("/pface/**").addResourceLocations("classpath:/pface/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //super.addResourceHandlers(registry);
    }

    /**
     * 跨域CORS配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/**")
                //放行哪些原始域
                .allowedOrigins("*")
                //.allowedOrigins("http://192.168.1.97")
                //是否发送Cookie信息
                //设置了Allow-Credentials，Allow-Origin就不能为*,需要指明具体的url域
                .allowCredentials(false)
                //放行哪些原始域(请求方式)
                //.allowedMethods("GET","POST", "PUT", "DELETE","OPTIONS")
                .allowedMethods("*")
                //放行哪些原始域(头部信息)
                .allowedHeaders("*")
                .maxAge(86400)
                //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
                .exposedHeaders("Access-Control-Allow-Headers", "Timestamp,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,AuthToken,Access-Control-Allow-Headers");

    }


//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index.html");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();

        //不显示为null的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //序列化枚举是以ordinal()来输出
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        JsonDeserializer<IBaseEnum> deserialize = new BaseEnumDeserializer();
        simpleModule.addDeserializer(IBaseEnum.class, deserialize);
        objectMapper.registerModule(simpleModule);

        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //放到第一个
        converters.add(0, jackson2HttpMessageConverter);
       //super.extendMessageConverters(converters);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConvertFactory());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Override
    public void setEnvironment(Environment environment) {
        pubfile=environment.getProperty("face.pubfile");
        //System.out.println("pubfile="+pubfile);
    }
}

