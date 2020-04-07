package cn.vshop.security.core.properties;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/7 17:45
 */
public interface SecurityConstants {

    /**
     * 默认的获取/处理验证码的url前缀
     * <p>
     * /code/+验证码类型
     * 如：
     * 1. /code/email 获取邮箱验证码
     * 2. /code/image 获取图片验证码
     */
    public static final String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code/";

    /**
     * 当请求需要身份认证时，默认转跳的url
     *
     * @see SecurityController
     */
    public static final String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";

    /**
     * 默认的用户名 <b>密码登录请求处理url</b>
     */
    public static final String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";

    /**
     * 默认的用户 <b>邮箱验证码登录请求处理url</b>
     */
    public static final String DEFAULT_LOGIN_PROCESSING_URL_EMAIL = "/authentication/email";

    /**
     * 默认的前端携带 <b>图片验证码</b> 的参数名
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";

    /**
     * 默认的前端携带 <b>邮箱验证码</b> 的参数名
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_EMAIL = "emailCode";

    /**
     * 默认的全段携带 <b>目标邮箱</b> 的参数名
     */
    public static final String DEFAULT_PARAMETER_NAME_EMAIL = "email";

}
