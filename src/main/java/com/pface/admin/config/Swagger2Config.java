package com.pface.admin.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2Config {

//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                //.apis(RequestHandlerSelectors.basePackage("com.pface.admin.modules.front.web"))
//                .apis(RequestHandlerSelectors.withClassAnnotation(Controller.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Jmgo-Admin接口文档")
//                .description("Jmgo-Admin | 节目购系统")
//                .termsOfServiceUrl("http://www.greenflash.cn")
//                .version("1.0")
//                .build();
//    }

    // http://localhost:端口/swagger-ui.html
    // Swagger2默认将所有的Controller中的RequestMapping方法都会暴露，
    // 然而在实际开发中，我们并不一定需要把所有API都提现在文档中查看，这种情况下，使用注解
    // @ApiIgnore来解决，如果应用在Controller范围上，则当前Controller中的所有方法都会被忽略，
    // 如果应用在方法上，则对应用的方法忽略暴露API
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("人脸后台接口系统")
//                .title("节目购后台接口系统")
                .description("接口文档内部信息请勿公开")
                .termsOfServiceUrl("8880:/*")
                .version("2.0")
                .license("（ww）").build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.pface.admin.modules.jiekou.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}