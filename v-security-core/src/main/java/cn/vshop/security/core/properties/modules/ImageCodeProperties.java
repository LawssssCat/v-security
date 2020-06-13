package cn.vshop.security.core.properties.modules;

import lombok.Getter;
import lombok.Setter;

/**
 * 图形验证码
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 14:47
 */
@Getter
@Setter
public class ImageCodeProperties {

    /**
     * 验证码宽
     */
    private int width = 67;

    /**
     * 验证码高
     */
    private int height = 23;

    /**
     * 验证码个数
     */
    private int length = 4;

    /**
     * 验证按失效时间
     */
    private int expireIn = 60;

    /**
     * 要进行图形验证码校验的url
     */
    private String[] urls ;

}
