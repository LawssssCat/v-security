package cn.vshop.security.service.impl;

import cn.vshop.security.dto.User;
import cn.vshop.security.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/30 14:38
 */
@Service
public class UserServiceImpl implements UserService {

    private static User user;

    static {
        user = new User();
        user.setId("1");
        user.setUsername("boo");
    }

    @Override
    public User selectByUsername(String username) {
        return user;
    }

    @Override
    public User selectById(String id) {
        return user;
    }
}
