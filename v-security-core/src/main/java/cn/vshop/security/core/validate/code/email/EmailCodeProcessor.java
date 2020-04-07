package cn.vshop.security.core.validate.code.email;

import cn.vshop.security.core.properties.SecurityConstants;
import cn.vshop.security.core.validate.code.ValidateCode;
import cn.vshop.security.core.validate.code.ValidateCodeException;
import cn.vshop.security.core.validate.code.ValidateCodeProcessor;
import cn.vshop.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 邮箱验证码处理器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 20:36
 */
@Slf4j
@Component("emailCodeProcessor")
public class EmailCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    /**
     * 邮箱校验码存储在session中对应的key
     */
    private final static String SESSION_KEY_EMAIL = ValidateCodeProcessor.SESSION_KEY_PREFIX + "EMAIL";

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    @Qualifier("emailCodeSender")
    private EmailCodeSender emailCodeSender;

    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String email = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), SecurityConstants.DEFAULT_PARAMETER_NAME_EMAIL);
        emailCodeSender.send(email, validateCode.getCode());
        log.info("发送邮箱校验码:{},有效时间:{}s,目标邮箱:{}", validateCode.getCode(), validateCode.getExpireTime(), email);
    }

    /**
     * 邮箱验证码校验的逻辑，emailCode
     *
     * @param request
     */
    @Override
    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
        // 从session中获取封装好的ValidateCode
        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request, SESSION_KEY_EMAIL);
        // 从request中获取请求参数ValidateCode
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_EMAIL);
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, SESSION_KEY_EMAIL);
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        sessionStrategy.removeAttribute(request, SESSION_KEY_EMAIL);
    }
}
