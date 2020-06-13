package cn.vshop.security.core.social.qq.api;

/**
 * 映射QQ用户的Api接口
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/8 14:46
 */
public interface QQ {
    /**
     * 获取用户的详细信息
     *
     * @return 用户详细信息的封装
     */
    QQUserInfo getUserInfo();
}
