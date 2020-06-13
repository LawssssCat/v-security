package cn.vshop.security.core.properties.modules;

import lombok.Getter;
import lombok.Setter;

/**
 * social 认证授权配置
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/9 8:11
 */
@Getter
@Setter
public class SocialProperties {

    /**
     * auth认证路径前缀：
     * {contextPath}/{filterProcessesUrl}/{providerId}
     */
    private String filterProcessesUrl = "/auth" ;

    /**
     * qq 认证授权配置
     */
    private QQProperties qq = new QQProperties();
}
