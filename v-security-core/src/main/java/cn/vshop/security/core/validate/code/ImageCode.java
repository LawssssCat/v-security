package cn.vshop.security.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 1:17
 */
@Getter
@Setter
@AllArgsConstructor
public class ImageCode {

    /**
     * 图片，展示给用户看的
     */
    private BufferedImage image;

    /**
     * 随机数，需要存在session中，作为验证的依据
     */
    private String code;

    /**
     * 过期时间
     * <p>
     * LocalDateTime 只存储如期，时间，不存储时区，JDK8后用以替代Date
     * 当时间不需要跟其他应用交互，建议使用。
     */
    private LocalDateTime expireTime;

    /**
     * 构造函数
     *
     * @param image    图片
     * @param code     验证码
     * @param expireIn 期望多长时间后过期（seconds）
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * 验证码是否过期
     *
     * @return 过期为true
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
