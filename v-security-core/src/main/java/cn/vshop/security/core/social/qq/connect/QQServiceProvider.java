package cn.vshop.security.core.social.qq.connect;

import cn.vshop.security.core.social.qq.api.QQ;
import cn.vshop.security.core.social.qq.api.QQImpl;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.web.client.RestTemplate;

/**
 * 服务提供商（Provider）
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/8 17:50
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    /**
     * 申请QQ登录成功后，分配给应用的appId
     */
    private String appId;

    /**
     * 引导用户到哪个地址进行授权
     * 参考：https://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
     */
    private static final String URL_AUTORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 获得授权码后，去到哪个地址获取令牌
     * 参考：https://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";


    public QQServiceProvider(String appId, String appSecret) {
        // 传入一个OAuth2Template，用于跟服务提供商通信
        super(new QQOAuth2Template(
                // app的用户名
                appId,
                // app的密码
                appSecret,
                // 将用户导向认证服务器的地址
                URL_AUTORIZE,
                // 申请令牌的认证服务器的地址
                URL_ACCESS_TOKEN
        ));
        this.appId = appId;
    }

    /**
     * 不是单例的，能确保线程安全
     *
     * @param accessToken 服务提供商Provider提供的令牌
     * @return Api接口的QQ实现
     */
    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}
