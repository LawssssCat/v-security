package cn.vshop.security.core.validate.code.email;

import cn.vshop.security.core.properties.SecurityProperties;
import cn.vshop.security.core.validate.code.ValidateCode;
import cn.vshop.security.core.validate.code.ValidateCodeException;
import cn.vshop.security.core.validate.code.ValidateCodeProcessor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 拦截邮箱认证请求，校验邮箱认证码是否正确
 * <p>
 * 每次请求只拦截一次 OncePerRequestFilter
 * 需要初始化 InitializingBean
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/7 0:51
 */
@Slf4j
@Setter
public class EmailCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * spring的工具类，用来匹配Ant风格路径，如：“/user/*”
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 需要拦截的URL
     * (默认添加 /authentication/email)
     */
    private Set<String> urls = new HashSet<>();

    /**
     * (需要手动注入)
     */
    private SecurityProperties securityProperties;

    /**
     * (需要手动注入)
     */
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 如果在Spring环境中，会在配置加载后执行
     * （这里需要手动执行）
     *
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = securityProperties.getCode().getEmail().getUrls();
        for (String url : configUrls) {
            urls.add(url);
        }
        urls.add("/authentication/email");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 循环判断是否执行过滤
        boolean action = false;
        for (String url : urls) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                action = true;
                break;
            }
        }

        // 如果是邮箱校验请求，执行邮箱校验逻辑
        if (action) {
            try {
                // 尝试校验
                validate(new ServletWebRequest(request, response));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 校验通过or不是email校验请求
        filterChain.doFilter(request, response);
    }

    /**
     * 邮箱校验码存储在session中对应的key
     */
    private final static String SESSION_KEY_EMAIL = ValidateCodeProcessor.SESSION_KEY_PREFIX + "EMAIL";

    /**
     * 校验的逻辑，emailCode
     *
     * @param request
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        // 从session中获取封装好的ValidateCode
        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request, SESSION_KEY_EMAIL);
        // 从request中获取请求参数ValidateCode
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "emailCode");
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, SESSION_KEY_EMAIL);
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        sessionStrategy.removeAttribute(request, SESSION_KEY_EMAIL);
    }
}
