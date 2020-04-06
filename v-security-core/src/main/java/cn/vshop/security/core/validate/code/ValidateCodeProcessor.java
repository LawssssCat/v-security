package cn.vshop.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同校验码的处理逻辑
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 17:35
 */
public interface ValidateCodeProcessor {
    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_" ;

    /**
     * 创建校验码
     * @param request ServletWebRequest是spring的工具类，封装请求和响应
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception  ;
}
