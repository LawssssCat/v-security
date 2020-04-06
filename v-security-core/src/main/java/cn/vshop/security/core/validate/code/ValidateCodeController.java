package cn.vshop.security.core.validate.code;

import cn.vshop.security.core.validate.code.email.EmailCodeProcessor;
import cn.vshop.security.core.validate.code.image.ImageCodeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 1:33
 */
@Slf4j
@RestController
public class ValidateCodeController {

    /**
     * 图片校验码处理器
     */
    @Autowired
    @Qualifier("imageCodeProcessor")
    private ImageCodeProcessor imageCodeProcessor;

    /**
     * 图片验证码接口
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        imageCodeProcessor.create(new ServletWebRequest(request, response));
    }

    /**
     * 邮箱校验码处理器
     */
    @Autowired
    @Qualifier("emailCodeProcessor")
    private EmailCodeProcessor emailCodeProcessor;

    /**
     * 邮箱验证码接口
     */
    @GetMapping("/code/email")
    public String createSmsCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        emailCodeProcessor.create(new ServletWebRequest(request, response));
        return "ok";
    }

}
