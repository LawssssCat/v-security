package cn.vshop.security.dto;

import lombok.Data;

/**
 * user 查询的VO类
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/3/29 1:39
 */
@Data
public class UserQueryCondition {
    private String username;
    private Integer age;
}
