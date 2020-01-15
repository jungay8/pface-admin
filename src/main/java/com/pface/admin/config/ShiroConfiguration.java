package com.pface.admin.config;



import com.pface.admin.core.shiro.cache.JedisCacheManager;
import com.pface.admin.core.shiro.credentials.RetryLimitHashedCredentialsMatcher;
import com.pface.admin.core.shiro.realm.UserRealm;
import com.pface.admin.core.shiro.session.CacheSessionDAO;
import com.pface.admin.core.shiro.session.JedisSessionDAO;
import com.pface.admin.core.shiro.session.SessionDAO;
import com.pface.admin.core.shiro.session.SessionManager;
import com.pface.admin.core.shiro.spring.SpringCacheManagerWrapper;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;

import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


//@Configuration
//@PropertySource(value = "classpath:xxx.properties")
public class ShiroConfiguration {

    /*
     * 不指定名字的话，自动创建一个方法名第一个字母小写的bean
     */
    /**
     * 集群环境，session交给spring-session管理
     */
    @Bean("shiroCacheManager")
    @ConditionalOnProperty(prefix = "jmgo", name = "cluster", havingValue = "true")
    public CacheManager jedisCacheManager(){
        JedisCacheManager jedisCacheManager=new JedisCacheManager();
        jedisCacheManager.setCacheKeyPrefix("zht_dream_cache_");
        return jedisCacheManager;
    }

    @Bean("shiroCacheManager")
    @ConditionalOnProperty(prefix = "jmgo", name = "cluster", havingValue = "false")
    public CacheManager ehCacheManager(net.sf.ehcache.CacheManager manager){
        org.apache.shiro.cache.ehcache.EhCacheManager ehCacheManager=new org.apache.shiro.cache.ehcache.EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        ehCacheManager.setCacheManager(manager);
        return ehCacheManager;
    }



    @Bean("sessionDAO")
    @ConditionalOnProperty(prefix = "jmgo", name = "cluster", havingValue = "true")
    public SessionDAO sessionRedisDAO() {
        JedisSessionDAO jedisSessionDAO=new JedisSessionDAO();
        jedisSessionDAO.setSessionKeyPrefix("zht_dream_session_");
        jedisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return jedisSessionDAO;
    }

    @Bean("sessionDAO")
    @ConditionalOnProperty(prefix = "jmgo", name = "cluster", havingValue = "false")
    public SessionDAO sessionDAO(CacheManager cacheManager) {
        CacheSessionDAO sessionDAO=new CacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        sessionDAO.setCacheManager(cacheManager);
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }


    @Bean("sessionManager")
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        SessionManager sessionManager=new SessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(12000000);
        sessionManager.setGlobalSessionTimeout(18000000);
        return sessionManager;
    }


    /**
     * 会话ID生成器
     *
     * @return
     */
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        SimpleCookie simpleCookie=new SimpleCookie("pface.session.id");
       //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);

        return simpleCookie;
    }


    /**
     * 先走 filter ，然后 filter 如果检测到请求头存在 token，则用 token 去 login，走 Realm 去验证
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //shiro自定义过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        shiroFilterFactoryBean.setFilters(filters);
        //配置记住我或认证通过可以访问的地址
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/login","authc");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //未授权界面;
        filterChainDefinitionMap.put("/authenticated", "authc");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "user");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    /**
     * 凭证匹配器
     *
     * @return
     */
    @Bean
    public RetryLimitHashedCredentialsMatcher credentialsMatcher(SpringCacheManagerWrapper cacheManager) {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * Realm实现
     *
     * @return
     */
    @Bean
    public UserRealm userRealm(RetryLimitHashedCredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);
        userRealm.setCachingEnabled(false);
        return userRealm;
    }

    @Bean
    public SecurityManager securityManager(UserRealm userRealm,SessionManager sessionManager, CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }


    /**
     * rememberMe管理器
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 自动登陆自动登陆cookie
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("pface.rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }



    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

   @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
