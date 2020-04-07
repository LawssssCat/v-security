package cn.vshop.security.browser;

import cn.vshop.security.core.properties.SecurityConstants;
import cn.vshop.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.DelegatingFilterProxy;

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
 * @date 2020/4/7 21:24
 */
public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("myAuthenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    @Qualifier("myAuthenticationFailureHandler")
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 跟密码登录相关的配置
     * <p>
     * 优先配置了一些登录相关的常量，和登录成功/失败的处理
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 指定身份认证方式为表单
                .formLogin()
                // 自定义转跳接口，当发现未登录，会转跳到此
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                // 执行登录的URL
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                // 自定义成功处理器
                .successHandler(authenticationSuccessHandler)
                // 自定义失败处理器
                .failureHandler(authenticationFailureHandler);

    }
}
