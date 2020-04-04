package cn.vshop.security.core.properties;


/**
 * Browser 项目（浏览器安全）相关的配置项
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 22:01
 */
public class BrowserProperties {

    // 登录页
    private String loginPage = "/login.html";

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
