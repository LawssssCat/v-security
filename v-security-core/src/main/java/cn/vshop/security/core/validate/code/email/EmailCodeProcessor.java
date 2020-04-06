package cn.vshop.security.core.validate.code.email;

import cn.vshop.security.core.validate.code.ValidateCode;
import cn.vshop.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class EmailCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    @Qualifier("emailCodeSender")
    private EmailCodeSender emailCodeSender;

    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String email = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "email");
        emailCodeSender.send(email, validateCode.getCode());
        log.info("发送邮箱校验码:{},有效时间:{}s,目标邮箱:{}", validateCode.getCode(), validateCode.getExpireTime(), email);
    }
}
