package cn.vshop.security.core.social.qq.connect;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/6/2 7:19
 */
public class QQOAuth2Template extends OAuth2Template {

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        // true if the client credentials should be passed as parameters; false if passed via HTTP Basic
        this.setUseParametersForClientAuthentication(true);
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        AccessGrant accessGrant = super.postForAccessGrant(accessTokenUrl, parameters);
        return accessGrant;
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        // 不能清空，因为还其他请求响应需要用到
        // converterList.clear();
        converterList.add(new StringMapHttpMessageConverter()) ;
        return restTemplate ;
    }

}
