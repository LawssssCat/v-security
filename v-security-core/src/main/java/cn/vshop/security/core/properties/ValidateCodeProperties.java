package cn.vshop.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 验证码配置
 * 包含：
 * 1. 图形验证码
 * 2. 短信验证码
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 14:54
 */
@Getter
@Setter
public class ValidateCodeProperties {

    private ImageCodeProperties image = new ImageCodeProperties();

    private EmailCodeProperties email = new EmailCodeProperties();

}
