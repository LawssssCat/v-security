package cn.vshop.security.core.validate.code.email;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 拦截邮箱认证请求，校验邮箱认证码是否正确
 * <p>
 * 每次请求只拦截一次 OncePerRequestFilter
 * 需要初始化 InitializingBean
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/7 0:51
 * @deprecated 功能移到了EmailCodeProcessor
 */
@Slf4j
@Setter
@Deprecated
public class EmailCodeFilter {


}
