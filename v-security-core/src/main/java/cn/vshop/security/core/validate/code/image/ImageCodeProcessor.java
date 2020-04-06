package cn.vshop.security.core.validate.code.image;

import cn.vshop.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import lombok.extern.slf4j.Slf4j;
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
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

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

}
