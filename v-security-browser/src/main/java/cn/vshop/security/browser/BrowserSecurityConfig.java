package cn.vshop.security.browser;

import cn.vshop.security.core.authentication.email.EmailCodeAuthenticationSecurityConfig;
import cn.vshop.security.core.properties.SecurityConstants;
import cn.vshop.security.core.properties.SecurityProperties;
import cn.vshop.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * 自定义配置类{@link AbstractChannelSecurityConfig}的子类
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 12:15
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    @Qualifier("usernameUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

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

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private EmailCodeAuthenticationSecurityConfig emailCodeAuthenticationSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 跟密码登录相关的配置
        super.configure(http);
        // 组装其他组件的配置
        http
                // 校验码相关的配置
                .apply(validateCodeSecurityConfig)

                // 配置邮箱验证码认证部分配置
                .and().apply(emailCodeAuthenticationSecurityConfig)

                // 浏览器特有的配置，记住我配置
                .and().rememberMe()
                // token持久化
                .tokenRepository(persistentTokenRepository())
                // token有效时间
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                // token的用户信息服务类
                .userDetailsService(userDetailsService)

                // 授权相关配置
                .and().authorizeRequests()
                // 设置，当访问到登录页面时，允许所有
                .antMatchers(
                        // 自定义转跳接口，当发现未登录，会转跳到此
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        // 邮箱认证
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_EMAIL,
                        // 登录表单页。自定义转跳接口发现配置为REDIRECT时，也会转跳至此
                        securityProperties.getBrowser().getLoginPage(),
                        // 获取各种验证码的接口
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "*"
                ).permitAll()
                // 全部请求，都需要认证
                .anyRequest().authenticated()

                // 关闭 csrf 防护
                .and().csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
