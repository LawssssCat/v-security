package cn.vshop.security.core.authentication.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

/**
 * 根据邮箱查找用户认证信息
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/7 11:07
 * @deprecated 未完成
 */
@Slf4j
//@Component("emailUserDetailsService")
@Deprecated
public class EmailUserDetailsService implements UserDetailsService {

    // 模拟注入DAO

    /**
     * 根据邮箱查找用户认证信息
     *
     * @param email 前端请求中携带的邮箱
     * @return 用于给用户认证的信息
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("登录邮箱:{}", email);

        // 模拟从DAO中根据email查出用户信息
        String username = "lawsssscat";
        String password = null;

        log.info("用户名:{}", username);

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("user");

        return new User(
                username,
                password,
                // 账号未被删除
                true,
                // 账号未过期
                true,
                // 密码未过期
                true,
                // 账号未被冻结
                true,
                // 用户的授权
                authorities);
    }
}
