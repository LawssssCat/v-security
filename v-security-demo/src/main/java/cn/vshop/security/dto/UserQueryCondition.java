package cn.vshop.security.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "用户名称")
    private String username;
    @ApiModelProperty(value = "用户年龄")
    private Integer age;
}
