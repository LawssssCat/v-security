package cn.vshop.security.core.authentication.email;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截邮箱验证码请求，并且组装Token
 * <p>
 * 直接复制 {@link UsernamePasswordAuthenticationFilter},并作出相依修改
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 22:46
 */
public class EmailCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // ~ Static fields/initializers
    // =====================================================================================

    // 请求中携带参数的名字
    public static final String SPRING_SECURITY_FORM_EMAIL_KEY = "email";

    private String emailParameter = SPRING_SECURITY_FORM_EMAIL_KEY;
    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    public EmailCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher(
                // 匹配的请求
                "/authentication/email", "POST"));
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // 判断当前请求是否为POST请求，如果不是就抛出异常
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String email = obtainEmail(request);

        if (email == null) {
            email = "";
        }

        email = email.trim();

        // 生成一个（未认证）Token
        EmailCodeAuthenticationToken authRequest = new EmailCodeAuthenticationToken(email);

        // 把请求信息放入Token，比如说IP、session
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        // 使用AuthenticationManager进行认证流程
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * Enables subclasses to override the composition of the username, such as by
     * including additional values and a separator.
     *
     * @param request so that request attributes can be retrieved
     * @return the username that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected String obtainEmail(HttpServletRequest request) {
        return request.getParameter(emailParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     *
     * @param request     that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     *                    set
     */
    protected void setDetails(HttpServletRequest request, EmailCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the parameter name which will be used to obtain the username from the login
     * request.
     *
     * @param emailParameter the parameter name. Defaults to "username".
     */
    public void setEmailParameter(String emailParameter) {
        Assert.hasText(emailParameter, "Username parameter must not be empty or null");
        this.emailParameter = emailParameter;
    }


    /**
     * Defines whether only HTTP POST requests will be allowed by this filter. If set to
     * true, and an authentication request is received which is not a POST request, an
     * exception will be raised immediately and authentication will not be attempted. The
     * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
     * authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getEmailParameter() {
        return emailParameter;
    }

}