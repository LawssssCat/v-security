package cn.vshop.security.core.properties.modules;


import cn.vshop.security.core.properties.LoginType;
import lombok.Getter;
import lombok.Setter;

/**
 * Browser 项目（浏览器安全）相关的配置项
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 22:01
 */
@Getter
@Setter
public class BrowserProperties {

    /**
     * 记住我的生效时间
     */
    private int rememberMeSeconds = 3600;

    /**
     * 自定义登录成功后的行为
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 登录页
     */
    private String loginPage = "/login.html";

}
