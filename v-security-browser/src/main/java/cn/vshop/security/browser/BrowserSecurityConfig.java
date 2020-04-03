package cn.vshop.security.browser;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring security 提供的 web 应用适配器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 12:15
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 指定身份认证方式为表单
        http.formLogin()
                .and()
                // 并且认证请求
                .authorizeRequests()
                // 全部请求，都需要认证
                .anyRequest().authenticated();

    }
}
