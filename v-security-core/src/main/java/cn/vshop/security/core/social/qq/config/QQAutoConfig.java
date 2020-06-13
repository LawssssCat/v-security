package cn.vshop.security.core.social.qq.config;

import cn.vshop.security.core.properties.SecurityProperties;
import cn.vshop.security.core.properties.modules.QQProperties;
import cn.vshop.security.core.social.qq.api.QQ;
import cn.vshop.security.core.social.qq.connect.QQConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * {@link SocialConfigurerAdapter}的适配器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/9 8:25
 */
@Slf4j
@Configuration
// 希望配置了相应配置，此配置类才生效
@ConditionalOnProperty(prefix = "v.security.social.qq", name = "app-id")// appId
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<QQ> createConnectionFactory() {
        log.info("SocialAutoConfigurerAdapter：新建ConnectionFactory");
        // qq相关配置
        QQProperties qqConfig = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(
                qqConfig.getProviderId(),
                qqConfig.getAppId(),
                qqConfig.getAppSecret()
        );
    }

}
