package cn.vshop.security.validator;


import cn.vshop.security.dto.User;
import cn.vshop.security.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义的注解验证方法
 * 实现ConstraintValidator<T,T>
 * 两个泛型类型为:
 * 第一个:自定义的注解
 * 第二个:传递的值例如你定义在field字段上,那么这个类型就是你定义注解的那个字段类型
 * ConstraintValidator<NotUserNameDuplicated, Object>
 *   
 * 在这边只要实现了ConstraintValidator<T,T>,那么你的这个方法就会被spring容器纳入管理
 * 因此你就可以很方便的在这个验证方法中注入spring管理的类去进行业务逻辑验证
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/3/30 14:25
 */
public class NotUserNameDuplicatedValidator implements ConstraintValidator<NotUserNameDuplicated, Object> {

    // 此类会自动被创建为bean放入容器，因此，可以直接使用@Autowired
    @Autowired
    private UserService userService;

    private String username;
    private String userid;

    /**
     * 初始化方法
     */
    @Override
    public void initialize(NotUserNameDuplicated constraint) {
        userid = constraint.userid();
        username = constraint.username();
    }

    /**
     * 验证方法
     * 验证成功返回: true
     * 验证失败返回: false
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            String name = BeanUtils.getProperty(obj, username);
            String id = BeanUtils.getProperty(obj, userid);

            if (!StringUtils.isEmpty(id)) {
                // id不为空，认为是修改
                User u1 = userService.selectById(id);
                // 用户名和id上的用户名一致，则返回true
                if (u1 != null && name.equals(u1.getUsername())) {
                    return true;
                }
            }

            User u2 = userService.selectByUsername(name);
            return u2 == null;

        } catch (final Exception ignore) {
            // name 为空 或者其他错误
            // return false ;
        }
        return false;
    }
}
