package cn.vshop.security.core.social.qq.connect;

import cn.vshop.security.core.social.qq.api.QQ;
import cn.vshop.security.core.social.qq.api.QQUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * QQ用户数据模型转换到标准的用户数据模型的适配器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/8 20:31
 */
@Slf4j
public class QQAdapter implements ApiAdapter<QQ> {

    /**
     * 测试当前的api请求是否可用
     *
     * @param api 获取QQ用户信息api
     * @return 该api是否可用
     */
    @Override
    public boolean test(QQ api) {
        boolean flag = api.getUserInfo() != null;
        // 改为true，略过测试
        log.info("测试QQ服务提供商API测试结果:{}", flag);
        return flag;
    }

    /**
     * 将QQ中获取的用户数据设置到标准的用户数据中
     *
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        // 拿到QQ的用户信息
        QQUserInfo userInfo = api.getUserInfo();
        // 设置用户名
        values.setDisplayName(userInfo.getNickname());
        // 设置头像
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        // 设置个人主页，QQ没有这东西
        values.setProfileUrl(null);
        // 服务商的用户id，也就是openId
        values.setProviderUserId(userInfo.getOpenId());
    }

    /**
     * 与setConnectionValues方法类似
     * (后面用到绑定和解绑时候再具体写)
     *
     * @param api
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    /**
     * 更新状态
     *
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(QQ api, String message) {
        // do nothing
    }
}
