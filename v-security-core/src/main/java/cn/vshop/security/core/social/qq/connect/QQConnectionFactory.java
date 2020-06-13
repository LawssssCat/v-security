package cn.vshop.security.core.social.qq.connect;

import cn.vshop.security.core.social.qq.api.QQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * 生成标准用户模型（Connection）的工厂
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/8 22:05
 */
@Slf4j
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    /**
     * Create a {@link OAuth2ConnectionFactory}.
     *
     * @param providerId the provider id e.g. "facebook"
     * @param appId      应用Id，申请QQ登录成功后，分配给应用的appId
     * @param appSecret  应用密码，申请QQ登录的时候，指定给应用的密码
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(
                // 服务提供商的唯一标识，通过配置文件配置
                providerId,
                // 服务提供商的接口实例
                // the ServiceProvider model for conducting the authorization flow and obtaining a native service API instance.
                new QQServiceProvider(appId, appSecret),
                // api接口的适配器
                // the ApiAdapter for mapping the provider-specific service API model to the uniform {@link Connection} interface.
                new QQAdapter());
        log.info("QQConnectionFactory创建成功");
    }
}
