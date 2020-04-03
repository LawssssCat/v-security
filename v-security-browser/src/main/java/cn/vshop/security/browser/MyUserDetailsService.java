package cn.vshop.security.browser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/3 17:05
 */
@Slf4j
// 注入 spring 容器
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Autowired
    //private DAO 对象 .... 这里就直接模拟了

    /**
     * 根据用户名查找用户信息，作为登录的认证的依据
     * 因为在spring环境中，查找信息的方式只需要注入即可
     *
     * @param username 用户要的认证的用户名
     * @return 认证依据的详细信息
     * @throws UsernameNotFoundException 没有 username 对应的用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户名：{}", username);

        // 模拟：查出来的用户密码
        String password = passwordEncoder.encode("123456");
        log.info("数据库密码：{}", password);

        // 授权信息，告诉SpringSecurity，当前用户一旦认证成功，拥有哪些权限
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                // 一个工具，把字符串以空格隔开，分别存储为权限
                .commaSeparatedStringToAuthorityList(
                        // 模拟从数据库读出一下权限
                        "admin user");

        // 返回UserDetails接口
        // User是SpringSecurity提供的UserDetails接口实现
        return new User(
                username,
                password,
                // 账号是否被删除
                true,
                // 账号是否过期
                true,
                // 密码是否过期
                true,
                // 账号是否被冻结
                true,
                authorities);
    }

}
