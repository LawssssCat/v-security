package cn.vshop.security.core.validate.code.image;

import cn.vshop.security.core.properties.SecurityProperties;
import cn.vshop.security.core.validate.code.ValidateCodeException;
import cn.vshop.security.core.validate.code.ValidateCodeProcessor;
import cn.vshop.security.core.validate.code.image.ImageCode;
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

import static com.mysql.jdbc.interceptors.SessionAssociationInterceptor.getSessionKey;

/**
 * 图形校验码的过滤器
 * <p>
 * 拦截需要图片校验码的请求，校验图片校验码是否正确
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 11:39
 * @deprecated 功能整合到了ValidateCodeFilter
 */
@Slf4j
@Setter
public class ImageCodeFilter {


}
