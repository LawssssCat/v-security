package cn.vshop.security.core.validate.code;

import cn.vshop.security.core.properties.SecurityConstants;
import cn.vshop.security.core.properties.SecurityProperties;
import cn.vshop.security.core.properties.ValidateCodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 校验码校验过滤器
 * <p>
 * 1.判断类型
 * 2.对不同类型的校验码进行校验
 * 3.判断校验码是否正确
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/7 1:22
 */
@Slf4j
@Component("validateCodeFilter")
public class ValidateCodeFilter
        // SpringMVC 提供的工具类，能确保此Filter每次请求只被调用一次
        extends OncePerRequestFilter
        // 在其他参数都加载完毕时，组装URLs的值
        implements InitializingBean {

    /**
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 我们前面自定义的验证失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 验证请求url与匹配url是否匹配的工具类
     * <p>
     * spring的工具类，用来匹配Ant风格路径，如：“/user/*”
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * 存放所有需要校验校验码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    /**
     *
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 初始化要拦截的url配置信息
     * 需要手动初始化
     *
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        // 图片校验码
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrls(), ValidateCodeType.IMAGE);

        // 邮箱校验码
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_EMAIL, ValidateCodeType.EMAIL);
        addUrlToMap(securityProperties.getCode().getEmail().getUrls(), ValidateCodeType.EMAIL);

    }

    /**
     * 将系统中配置的需要校验码校验的URL根据校验的类型放入map中
     *
     * @param urls 需要校验的url
     * @param type 对应的校验类型
     */
    private void addUrlToMap(String[] urls, ValidateCodeType type) {
        if (ArrayUtils.isNotEmpty(urls)) {
            for (String url : urls) {
                this.urlMap.put(url, type);
            }
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 循环判断是否有验证码类型需要校验
        ValidateCodeType type = getValidateCodeType(request);

        // 如果请求的是执行登录URL
        if (type != null) {
            // 尝试校验
            log.info("校验请求({})中的验证码类型{}", request.getRequestURI(), type);
            try {
                validateCodeProcessorHolder
                        // 获取相应类型的校验码处理器
                        .findValidateCodeProcessor(type)
                        // 处理校验码校验
                        .validate(new ServletWebRequest(request, response));
                log.info("验证码校验通过");
            } catch (ValidateCodeException e) {
                // 捕获到自定义的验证码校验异常
                // 用我们之前自定义的错误处理器，进行校验失败的处理
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 校验通过or不是登录请求
        filterChain.doFilter(request, response);
    }

    /**
     * 根据请求，获取校验码的类型，如果当前请求不需要校验码，则返回null
     *
     * @param request 请求
     * @return 校验码类型，如果不需要校验，则返回null
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        for (String url : urlMap.keySet()) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                return urlMap.get(url);
            }
        }
        return null;
    }
}
