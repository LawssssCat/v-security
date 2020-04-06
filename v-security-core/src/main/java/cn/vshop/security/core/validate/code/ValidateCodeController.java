package cn.vshop.security.core.validate.code;

import cn.vshop.security.core.validate.code.email.EmailCodeSender;
import cn.vshop.security.core.validate.code.image.ImageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 1:33
 */
@Slf4j
@RestController
public class ValidateCodeController {

    /**
     * 图片校验码信息存入session中的key
     */
    public static final String IMAGE_SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    /**
     * 邮箱校验码信息存入session中的key
     */
    public static final String EMAIL_SESSION_KEY = "SESSION_KEY_EMAIL_CODE";

    /**
     * 注入接口，接口的实现完成【图片】验证码的生成和封装
     */
    @Autowired
    @Qualifier("imageCodeGenerator")
    private ValidateCodeGenerator imageCodeGenerator;

    /**
     * 注入接口，接口的实现完成【邮箱】验证码的生成和封装
     */
    @Autowired
    @Qualifier("emailCodeGenerator")
    private ValidateCodeGenerator emailCodeGenerator;

    /**
     * 发送邮箱的工具实现
     */
    @Autowired
    @Qualifier("emailCodeSender")
    private EmailCodeSender emailCodeSender;

    /**
     * Spring 的工具类，用以操作session
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 图片验证码接口
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成随机的验证码，并封装为 ImageCode
        ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(request);
        // 获取session，并把键值对存入session
        sessionStrategy.setAttribute(
                // ServletWebRequest 是一个适配器（Adapter），把servlet封装成spring的WebRequest（继承了RequestAttributes）
                // 通过把请求传进来，sessionStrategy会从请求中获取session
                new ServletWebRequest(request),
                // 存入session中的key
                IMAGE_SESSION_KEY,
                // 存入session中的值
                imageCode);
        // javax的io工具包
        // 将BufferedImage以指定格式写入输出流中
        ImageIO.write(
                // 以BufferedImage类型的图片
                imageCode.getImage(),
                // 输出的图片格式
                "JPEG",
                // 输出流，输出到响应体中
                response.getOutputStream());

    }

    /**
     * 邮箱验证码接口
     */
    @GetMapping("/code/email")
    public String createSmsCode(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException {
        // 生成邮箱形式的验证码(普通的验证码)
        ValidateCode emailCode = emailCodeGenerator.generate(request);
        // 将验证码放入session
        sessionStrategy.setAttribute(new ServletWebRequest(request), EMAIL_SESSION_KEY, emailCode);
        // 请求参数中获取目标eamil
        String email = ServletRequestUtils.getRequiredStringParameter(request, "email");
        log.info("email:{}", email);
        // 发送短信
        emailCodeSender.send(email, emailCode.getCode());
        return "ok";
    }

}
