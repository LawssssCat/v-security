package cn.vshop.security.browser;

import cn.vshop.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * spring security 提供的 web 应用适配器
 * 可以重写configure方法，实现自定义配置
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 12:15
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("myAuthenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    @Qualifier("myAuthenticationFailureHandler")
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 指定身份认证方式为表单
        http.formLogin()
                // 自定义登录页面
                .loginPage("/authentication/require")
                // 执行登录的URL
                .loginProcessingUrl("/authentication/form")
                // 自定义成功处理器
                .successHandler(authenticationSuccessHandler)
                // 自定义失败处理器
                .failureHandler(authenticationFailureHandler)
                .and()
                // 并且认证请求
                .authorizeRequests()
                // 设置，当访问到登录页面时，允许所有
                .antMatchers("/authentication/require", securityProperties.getBrowser().getLoginPage()).permitAll()
                // 全部请求，都需要认证
                .anyRequest().authenticated()
                .and()
                // 关闭 csrf 防护
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
