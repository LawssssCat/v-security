package cn.vshop.security.core.validate.code.image;

import cn.vshop.security.core.validate.code.ValidateCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * 图片验证码
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 1:17
 */
@Getter
@Setter
public class ImageCode extends ValidateCode {

    /**
     * 图片，展示给用户看的
     */
    private BufferedImage image;


    /**
     * 构造函数
     *
     * @param image    图片
     * @param code     验证码
     * @param expireIn 期望多长时间后过期（seconds）
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

}
