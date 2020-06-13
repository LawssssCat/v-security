package cn.vshop.security.core.social.qq.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/8 14:54
 */
@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    /**
     * 获取 userInfo 的接口
     * <p>
     * 携带三个参数
     * 1. oauth_consumer_key=YOUR_APP_ID
     * 2. openid=YOUR_OPENID
     * 3. 还有一个参数：access_token=YOUR_ACCESS_TOKEN会在父类里面被添加上
     * <p>
     * 其中：%s为占位符
     */
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    /**
     * 获取 openId 的接口
     * <p>
     * 其中：%s为占位符
     */
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 申请QQ登录成功后，分配给应用的appId
     */
    private String appId;

    /**
     * 用户的ID，与QQ号码一一对应
     */
    private String openId;

    /**
     * 序列化
     * 多例
     */
    private ObjectMapper objectMapper ;
    {
        objectMapper = new ObjectMapper();
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 构造函数
     * 在获取到令牌后，通过令牌和RESTTemplate获取openId，同时填入appId
     *
     * @param accessToken 走完OAuth流程后拿到的令牌
     * @param appId       系统的配置信息
     */
    public QQImpl(String accessToken, String appId) {
        // QQ文档中要求accessToken参数放在请求url上，而默认的行为不符合，因此需要用第二个参数手动指定
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        // 下面获取openID
        // 用Token替换掉%s
        String url = String.format(URL_GET_OPENID, accessToken);
        // 响应字段参考:https://wiki.connect.qq.com/%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7openid_oauth2-0
        // "callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );"
        String result = getRestTemplate().getForObject(url, String.class);
        log.info("获取openId的REST响应:{}", result);
        // 暂时这样处理，后面会重构
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    @Override
    public QQUserInfo getUserInfo() {
        // 发送的请求，还有一个参数access_token会被自动填入
        String url = String.format(URL_GET_USERINFO, appId, openId);
        // 响应字段参考:https://wiki.connect.qq.com/get_user_info
        // 可以使用 fasterxml 下的 ObjectMapper 工具类
        String result = getRestTemplate().getForObject(url, String.class);
        log.info("userInfo:{}", result);
        try {
            QQUserInfo qqUserInfo = objectMapper.readValue(result, QQUserInfo.class);
            qqUserInfo.setOpenId(this.openId);
            return qqUserInfo;
        } catch (IOException e) {
            log.error("获取用户新消息失败,错误信息：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
