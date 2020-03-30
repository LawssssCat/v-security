package cn.vshop.security.service;

import cn.vshop.security.dto.User;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/30 14:38
 */
public interface UserService {
    /**
     * 数据库查询用户，通过用户名
     *
     * @param username 用户名
     * @return 查询到的用户
     */
    User selectByUsername(String username);

    /**
     * 数据库查询用户，通过ID
     *
     * @param id 用户ID
     * @return 查询到的用户
     */
    User selectById(String id);
}
