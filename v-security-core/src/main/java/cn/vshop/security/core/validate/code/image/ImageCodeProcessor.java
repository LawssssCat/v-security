package cn.vshop.security.core.validate.code.image;

import cn.vshop.security.core.properties.SecurityConstants;
import cn.vshop.security.core.validate.code.ValidateCodeException;
import cn.vshop.security.core.validate.code.ValidateCodeProcessor;
import cn.vshop.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * 图片校验码认证器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 20:54
 */
@Slf4j
@Component("imageCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    /**
     * 图片校验码存储在session中对应的key
     */
    private final static String SESSION_KEY_IMAGE = ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE";

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 发送图形验证码，将其写到响应中
     *
     * @param request   经过spring封装的请求和响应
     * @param imageCode 图片校验码码
     * @throws Exception
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
        log.info("发送图片校验码:{},有效时间:{}s", imageCode.getCode(), imageCode.getExpireTime());
    }

    /**
     * 图片验证码校验的逻辑，imageCode
     *
     * @param request
     */
    @Override
    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
        // 从session中获取封装好的ImageCode
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, SESSION_KEY_IMAGE);
        // 从request中获取请求参数imageCode
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE);
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpired()) {
            // 如果过期了，就移除验证码
            sessionStrategy.removeAttribute(request, SESSION_KEY_IMAGE);
            // 然后再抛异常
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        sessionStrategy.removeAttribute(request, SESSION_KEY_IMAGE);
    }
}
