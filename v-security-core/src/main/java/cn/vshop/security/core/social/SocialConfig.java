package cn.vshop.security.core.social;

import cn.vshop.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfiguration;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 配置类
 * <p>
 * {@link EnableSocial}的作用是往容器中注入{@link SocialConfiguration}配置
 * （这个配置会往容器中注入几个和Social相关的类，
 * 如：下面创建ConnectionRepository要用到的connectionFactoryLocator工厂选择器）。
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/8 22:23
 */
@Slf4j
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 获取UsersConnectionRepository（存储提供商用户和本地用户的映射关系）
     * <p>
     * 默认返回的是从内存中获取连接的Repository
     *
     * @param connectionFactoryLocator 在注解@EnableSocial注入的配置中被指定，帮我们定位ConnectionFactory
     * @return 从数据库中获取连接的Repository
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(
                // 数据源
                dataSource,
                // connectionFactory的定位器，因为内存中可能有多个ConnectionFactory（如：QQ、微信）
                connectionFactoryLocator,
                // 对存入数据库数据的加密解密器,为了便于后面观测数据，这里就不加密了
                Encryptors.noOpText());
        repository.setTablePrefix("v_");
        return repository;
    }

    /**
     * 社交登录配置类，供浏览器或app模块引入设计登录配置用。
     * Configurer that adds {@link SocialAuthenticationFilter} to Spring Security's filter chain.
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer vSocialSecurityConfig() {
        log.info("social 配置生效：注入SocialAuthenticationFilter");
        VshopSocialSecurityConfig socialSecurityConfig = new VshopSocialSecurityConfig(securityProperties.getSocial().getFilterProcessesUrl());
        return socialSecurityConfig;
    }

}
