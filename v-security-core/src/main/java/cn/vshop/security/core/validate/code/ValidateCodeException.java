package cn.vshop.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码校验异常
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 11:54
 */
public class ValidateCodeException
        // Spring Security所有校验异常的基类
        extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
