package cn.vshop.security.dto;

import cn.vshop.security.validator.NotUserNameDuplicated;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/29 1:07
 */
@NotUserNameDuplicated(username="username", userid="id", message = "用户名重复")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    /**
     * 1. 使用接口来声明多个视图
     */
    public interface UserSimpleView {
    }

    public interface UserDetailView extends UserSimpleView {
    }

    @JsonView(UserSimpleView.class)
    private String id;

    @NotBlank(message = "用户名不能为空")
    @JsonView(UserSimpleView.class) //2. 在值对象的get方法上指定视图
    private String username;

    /**
     * 因为，UserDetailView 继承了 UserSimpleView
     * 因此，被UserSimpleView注解的属性(如username)也是会被UserDetailView包含
     */
    @NotBlank(message = "密码不能为空")
    @JsonView(UserDetailView.class)
    private String password;

    /**
     * 余额
     */
    @JsonView(UserSimpleView.class)
    private Integer balance;

    @Past(message = "生日不能为未来时间")
    @JsonView(UserSimpleView.class)
    private Date birthday;

}
