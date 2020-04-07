package cn.vshop.security.browser;

import cn.vshop.security.core.authentication.email.EmailCodeAuthenticationSecurityConfig;
import cn.vshop.security.core.properties.SecurityProperties;
import cn.vshop.security.core.validate.code.email.EmailCodeFilter;
import cn.vshop.security.core.validate.code.image.ImageCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 * {@link WebSecurityConfigurerAdapter}:
 * spring security 提供的 {@link WebSecurity} 适配器
 * 一个帮我们实现了大部分{@link WebSecurityConfigurer}接口的抽象类
 * <p>
 * 提供了可以重写的configure方法，在方法内可以装配自定义的Filter配置
 * 其指定的Filter会被生产为{@link FilterChainProxy}
 * 最终以{@link DelegatingFilterProxy}作为spring Security Filter Chain起作用
 * <p>
 * 即：其实现是用来配置spring Security Filter Chain的
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 12:15
 */
@Configuration
public class BrowserSecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private EmailCodeAuthenticationSecurityConfig emailCodeAuthenticationSecurityConfig;

    @Autowired
    @Qualifier("myAuthenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    @Qualifier("myAuthenticationFailureHandler")
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("usernameUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * @return RememberMe 功能的 Repository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        // 连接数据库的实现类，还有一个实现类时存内存的InMemoryTokenRepositoryImpl
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 配置数据源
        tokenRepository.setDataSource(dataSource);
        // 在启动数据库时创建存储token的表
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    /**
     * @return 验证码校验过滤器
     */
    public Filter getImageCodeFilter() throws ServletException {
        ImageCodeFilter filter = new ImageCodeFilter();
        // 设置失败处理器
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // 注入配置属性
        filter.setSecurityProperties(securityProperties);
        // 初始化
        filter.afterPropertiesSet();
        return filter;
    }

    public Filter getEmailCodeFilter() throws ServletException {
        EmailCodeFilter filter = new EmailCodeFilter();
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setSecurityProperties(securityProperties);
        filter.afterPropertiesSet();
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 把邮箱验证码过滤器放在用户名密码过滤器前
                .addFilterBefore(getEmailCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                // 把图形验证码验证过滤器放在用户名密码过滤器前
                .addFilterBefore(getImageCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                // 指定身份认证方式为表单
                .formLogin()
                // 自定义转跳接口，当发现未登录，会转跳到此
                .loginPage("/authentication/require")
                // 执行登录的URL
                .loginProcessingUrl("/authentication/form")
                // 自定义成功处理器
                .successHandler(authenticationSuccessHandler)
                // 自定义失败处理器
                .failureHandler(authenticationFailureHandler)
                .and()
                // 记住我配置
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                // 并且认证请求
                .authorizeRequests()
                // 设置，当访问到登录页面时，允许所有
                .antMatchers(
                        // 自定义转跳接口，当发现未登录，会转跳到此
                        "/authentication/require",
                        // 登录表单页。自定义转跳接口发现配置为REDIRECT时，也会转跳至此
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/*"
                ).permitAll()
                // 全部请求，都需要认证
                .anyRequest().authenticated()
                .and()
                // 关闭 csrf 防护
                .csrf().disable()
                .apply(emailCodeAuthenticationSecurityConfig);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
