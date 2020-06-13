package cn.vshop.security.core.properties;

import cn.vshop.security.core.properties.modules.BrowserProperties;
import cn.vshop.security.core.properties.modules.SocialProperties;
import cn.vshop.security.core.properties.modules.ValidateCodeProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 总配置项
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 22:03
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "v.security")
public class SecurityProperties {

    /**
     * 这里读取的是 v.security.browser 配置项
     */
    private BrowserProperties browser = new BrowserProperties();

    /**
     * 这里读取的是 v.security.code 配置项
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();

    /**
     * 这里读取的是 v.security.social 配置项
     */
    private SocialProperties social = new SocialProperties();

}
