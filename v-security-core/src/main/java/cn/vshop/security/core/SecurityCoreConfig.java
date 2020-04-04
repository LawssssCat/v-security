package cn.vshop.security.core;

import cn.vshop.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 让我们配置的 properties 生效
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 22:08
 */
@Configuration
// 指定想要使其生效的配置器
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {
}
