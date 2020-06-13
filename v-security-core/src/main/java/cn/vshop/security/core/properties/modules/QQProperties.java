package cn.vshop.security.core.properties.modules;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/9 8:08
 */
@Getter
@Setter
public class QQProperties extends SocialProperties {

    /**
     * 服务提供商的唯一标识码
     */
    private String providerId = "qq";

}
