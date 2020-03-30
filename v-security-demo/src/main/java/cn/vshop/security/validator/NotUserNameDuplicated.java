package cn.vshop.security.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验用户名是否已存在，通过查询数据库的用户id和用户名
 * 如果id为空，认为是插入，不允许用户名重复
 * 如果id不为空，认为是修改，不允许用户名重复，但允许用户名与id上的用户名重复
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/3/30 9:45
 */
// 指定此注解可注解在类class、接口interface、注解@interface、枚举enum上
@Target({ElementType.TYPE})
// 指定为程序运行时起作用的注解
@Retention(RetentionPolicy.RUNTIME)
// 指定校验的执行器
@Constraint(validatedBy = NotUserNameDuplicatedValidator.class)
// (非必要)是否被javaDoc收录为api
@Documented
public @interface NotUserNameDuplicated {
    //message、groups和payload三个属性必须加上

    // 提示信息，可以从ValidationMessages.properties里提取，可以实现国际化效果
    String message() default "username is duplicated";

    // 可以用于做分组校验(不同情况制定不同校验规则)
    Class<?>[] groups() default {};

    // 挂载一些元数据(metaData)，如管理系统的访问权限、文件拥有者等数据，少用
    Class<? extends Payload>[] payload() default {};

    String username();

    String userid();
}
