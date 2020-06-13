package cn.vshop.security.core.validate.code;

import cn.vshop.security.core.validate.code.email.DefaultEmailCodeSender;
import cn.vshop.security.core.validate.code.email.EmailCodeGenerator;
import cn.vshop.security.core.validate.code.email.EmailCodeSender;
import cn.vshop.security.core.validate.code.image.ImageCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

/**
 * 校验码生成逻辑的配置类
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 17:39
 */
@Slf4j
@Configuration
public class ValidateCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /*---------------------------------------------------------------------------------------*/
    /*--------                      i m a g e                                        --------*/
    /*---------------------------------------------------------------------------------------*/

    /**
     * 定义图片校验码的生成方式
     * 如果用户没有注入名字为imageCodeGenerator的bean，那么就使用默认的
     *
     * @return 默认的 ValidateCodeGenerator 实现
     */
    @Bean("imageCodeGenerator")
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        return new ImageCodeGenerator();
    }

    /*---------------------------------------------------------------------------------------*/
    /*--------                      e m a i l                                        --------*/
    /*---------------------------------------------------------------------------------------*/

    /**
     * 定义邮箱校验码的生成方式
     */
    @Bean("emailCodeGenerator")
    @ConditionalOnMissingBean(name = "emailCodeGenerator")
    public ValidateCodeGenerator emailCodeGenerator() {
        return new EmailCodeGenerator();
    }

    /**
     * 定义邮箱校验码的发送方式
     */
    @Bean("emailCodeSender")
    @ConditionalOnMissingBean(name = "emailCodeSender")
    public EmailCodeSender emailCodeSender() {
        return new DefaultEmailCodeSender();
    }


    /*--------------------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------*/

    @Autowired
    @Qualifier("validateCodeFilter")
    private Filter validateCodeFilter;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("添加验证码校验过滤器");
        // 把验证码验证过滤器放在用户名密码过滤器前
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
