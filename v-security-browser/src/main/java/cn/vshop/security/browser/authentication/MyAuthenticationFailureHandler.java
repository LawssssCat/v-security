package cn.vshop.security.browser.authentication;

import cn.vshop.security.browser.support.SimpleResponse;
import cn.vshop.security.core.properties.LoginType;
import cn.vshop.security.core.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/4 11:33
 */
@Slf4j
@Component("myAuthenticationFailureHandler")
public class MyAuthenticationFailureHandler
        // 继承SpringSecurity默认登录失败后的处理器，在其上做扩展
        extends ExceptionMappingAuthenticationFailureHandler {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            // 因为没有登录成功，因此没有用信息
            // 取而代之的，是认证过程中发生的异常信息
            AuthenticationException exception
    ) throws IOException, ServletException {

        log.info("登录失败");
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            // 因为登录失败，不能返回默认的200信息，而是500(看需求)
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            // 响应头
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            // 相应的，我们这里返回异常的json信息
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));

        } else {
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
