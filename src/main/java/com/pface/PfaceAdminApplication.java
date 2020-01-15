package com.pface;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.pface.admin.modules.base.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author daniel.liu
 */
@SpringBootApplication(exclude = {QuartzAutoConfiguration.class,PageHelperAutoConfiguration.class})
@EnableTransactionManagement
@EnableWebMvc
@EnableCaching
@EnableScheduling
public class PfaceAdminApplication implements ApplicationListener<ContextRefreshedEvent> {
    private boolean isExecut;
    public static void main(String[] args) {
        SpringApplication.run(PfaceAdminApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        /*if(!isExecut) {
            ApplicationContext applicationContext = event.getApplicationContext();
            //注册转换器
            SimpleModule module = new SimpleModule();
            JsonDeserializer<IBaseEnum> deserialize = new BaseEnumDeserializer();
            module.addDeserializer(IBaseEnum.class, deserialize);
            ObjectMapper bean = applicationContext.getBean(ObjectMapper.class);
            bean.registerModule(module);
        }
        isExecut=true;*/
    }

    @Bean
    CommandLineRunner init(final StorageService storageService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                storageService.init();
                storageService.deleteAll();
            }
        };
    }
}
