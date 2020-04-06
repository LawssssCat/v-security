package cn.vshop.security.core.validate.code.email;

/**
 * 邮箱发送器接口
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 0:42
 */
public interface EmailCodeSender {
    /**
     * 发送验证码到目标邮箱
     *
     * @param to 目标邮箱
     * @param code  验证码
     */
    void send(String to, String code);
}
