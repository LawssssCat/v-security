package cn.vshop.security.core.authentication.email;

import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 邮箱验证码认证的提供者，
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 23:05
 */
@Setter
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService ;

    /**
     * 认证的主要逻辑
     *
     * @param authentication 我们自定义的 EmailCodeAuthenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // supports方法通过已经说明token为EmailCodeAuthenticationToken，因此可以强转
        EmailCodeAuthenticationToken authenticationToken = (EmailCodeAuthenticationToken) authentication;
        // 此时principal为email，调用（自定义）UserDetailsService，通过email获取UserDetails
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());

        if(user==null){
            // 如果查找不到数据，抛出内部服务异常
            // 这个InternalAuthenticationServiceException异常将被视为可处理异常，因此不会被最终抛出
            throw new InternalAuthenticationServiceException("无法获取用户信息") ;
        }

        // 重新生成（已认证）Token
        EmailCodeAuthenticationToken authenticationResult = new EmailCodeAuthenticationToken(user, user.getAuthorities());
        // 将（未认证）Token中的IP、session等信息放入（已认证）Token中
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * 判断是否当前认证请求是否是EmailCodeAuthenticationToken
     *
     * @param authentication 当前的请求Token
     * @return 如果是EmailCodeAuthenticationToken或其子类，则返回true，表示支持当前认证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
