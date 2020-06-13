package cn.vshop.security.core.authentication.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 关于短信验证码的配置
 * <p>
 * (因为既要在浏览器中用，也要在app中用，因此写在core内)
 * <p>
 * 写好后只需在应用配置(如BrowserSecurityConfig)中导入配置即可生效
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/7 1:47
 */
@Component
public class EmailCodeAuthenticationSecurityConfig
        // HttpSecurity关于DefaultSecurityFilterChain的配置适配器
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    //@Qualifier("usernameUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * 构造邮箱验证码校验过滤器
     *
     * @param http
     */
    private EmailCodeAuthenticationFilter getEmailCodeAuthenticationFilter(HttpSecurity http) {
        // 创建邮箱验证码校验过滤器
        EmailCodeAuthenticationFilter filter = new EmailCodeAuthenticationFilter();
        // 设置认证管理器
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        // 注册successHandler
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 注册failureHandler
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // 让认证过滤器有“记住我”功能
        filter.setRememberMeServices(http.getSharedObject(RememberMeServices.class));
        return filter;
    }

    /**
     * 构造邮箱验证码的provider
     *
     * @return
     */
    private EmailCodeAuthenticationProvider emailCodeAuthenticationProvider() {
        EmailCodeAuthenticationProvider provider = new EmailCodeAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /**
     * 对FilterChain的配置
     * 同{@link WebSecurityConfigurerAdapter}的configure(HttpSecurity http)
     *
     * @param http 封装http的请求响应，可以操作FilterChain
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 将我们自定义的provider添加到AuthenticationManager管理的provider集合内
                .authenticationProvider(emailCodeAuthenticationProvider())
                // 将我们自定义的filter添加到UsernamePasswordAuthenticationFilter的后面
                // 为什么是后面？
                // 因为其他配置均已UsernamePasswordAuthenticationFilter为基准，把校验码的校验如EmailCodeFilter配置在其之前，
                // 对应的这类的认证过滤器就应配置在其之后
                .addFilterAfter(getEmailCodeAuthenticationFilter(http), UsernamePasswordAuthenticationFilter.class);
    }

}
