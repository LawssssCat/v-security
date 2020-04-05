package cn.vshop.security.core.validate.code;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验码生成器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 17:25
 */
public interface ValidateCodeGenerator {
    /**
     * 生成验证码图片的逻辑代码
     *
     * @param request 请求
     * @return 将验证码图片封装为 ImageCode 返回
     */
    ImageCode createImageCode(HttpServletRequest request);
}
