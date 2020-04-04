package cn.vshop.security.browser.authentication;

import cn.vshop.security.core.properties.LoginType;
import cn.vshop.security.core.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/4 11:09
 */
@Slf4j
@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler
        // 继承 Spring Security 默认的处理器，在他上面添加 json返回功能
        extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SecurityProperties securityProperties;

    // 对象到json串的转换器，spring启动时自动注册
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            // 封装认证信息，包括认证请求中的信息（如session、ip）、UserDetails
            Authentication authentication)
            throws IOException, ServletException {
        log.info("登录成功！");

        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            // 判断如果我们定义的登录类型是 json，那么就用我们自己的方式 返回json
            // 响应类型信息
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            // 响应体信息
            // 把Authentication实例转为一个json字符串
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else {
            // 否则，就用父类的跳转
            super.onAuthenticationSuccess(request, response, authentication);
        }


    }

}
