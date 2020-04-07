package cn.vshop.security.browser;

import cn.vshop.security.browser.support.SimpleResponse;
import cn.vshop.security.core.properties.SecurityConstants;
import cn.vshop.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 21:22
 */
@Slf4j
@RestController
public class BrowserSecurityController {

    @Autowired
    private SecurityProperties securityProperties ;

    /**
     * 我们需要做判断，判断引发跳转的是否是html
     * 判断依据可以从 spring Security 提供的缓存中拿,因为 Spring Security 会把它的转跳请求放在 RequestCache 里面进行缓存
     * 所以，我们现在就可以把缓存中的 request 拿出来进行比较
     */
    private RequestCache requestCache = new HttpSessionRequestCache();


    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 当需要身份人认证时，跳转到这里处理
     *
     * @param request  请求
     * @param response 响应
     * @return 响应体
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    // 就是 401
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 就是之前引发跳转，并缓存下载的那个请求
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            // 之歌字符串就是引发跳转的url
            // 比方说，我访问 /user 被拦截转跳达到 /login
            // 这里的 targetURL 就是 "http://localhost:8080/user"
            String targetURL = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是：{}", targetURL);
            // 判断引发转跳的url是否想访问一个页面
            if (StringUtils.endsWithIgnoreCase(targetURL, ".html")) {
                // 如果用户是想访问一个页面
                // 那么，就让他重定向到指定的url
                // 注意，这里的url不同项目可能不同，即不可以写死。
                // 我们决定在propertiest类里面配置
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        // 封装一个对象，专门返回信息/数据
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }

}
