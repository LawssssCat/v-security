package cn.vshop.security.web.controller;

import cn.vshop.security.dto.User;
import cn.vshop.security.dto.UserQueryCondition;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/29 1:04
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private FieldError e;

    /**
     * 3. 在Controller方法上指定视图
     * 指定JsonView，声明对象Json转换后要留下的字符串
     */
    @JsonView(User.UserSimpleView.class)
    @GetMapping
    public List<User> query(
            UserQueryCondition uqc,
            /*
             * Pageable: 是springdata提供的一个分页查询接口
             * 默认的实现是 PageRequest
             * 分页相关的属性分别为:
             * page 当前页码值
             * size 当前页数据量
             * sort 排序规则
             *
             * @PageableDefault 指定一些默认值
             */
            @PageableDefault(page = 1, size = 3, sort = "username,asc") Pageable p
    ) {
        // 格式化打印对象
        log.info(ReflectionToStringBuilder.toString(uqc, ToStringStyle.MULTI_LINE_STYLE));
        log.info(ReflectionToStringBuilder.toString(p, ToStringStyle.MULTI_LINE_STYLE));

        // 模拟查询结果
        ArrayList<User> users = new ArrayList<>();
        // 简单的包含用户密码信息
        //users.add(new User("0", uqc.getUsername() + 1, "123", 100, new Date()));
        users.add(new User());
        users.add(new User());
        users.add(new User());

        return users;
    }

    /**
     * 3. 在Controller方法上指定视图
     * 指定JsonView，声明对象Json转换后要留下的字符串
     */
    @JsonView(User.UserDetailView.class)
    @GetMapping(
            // 正则表达式判断请求 id 为数字
            // (如果直接用 Integer id 接收, 需要进行mvc的异常处理，没必要)
            "/{id:\\d+}"
    )
    public User getInfo(@PathVariable("id") String id ) {

        User user = new User();
        user.setId(id);
        user.setUsername("boo");
        user.setPassword("111");
        user.setBalance(100);
        user.setBirthday(new Date());
        return user;
    }

    /**
     * 模拟数据库创建用户
     */
    @JsonView(User.UserSimpleView.class)
    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            // 有错误的话就把错误打印，并正常返回
            errors.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
        }

        user.setId("1");
        log.info(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));
        return user;
    }

    /**
     * 模拟数据库修改用户
     */
    @JsonView(User.UserSimpleView.class)
    @PutMapping("/{id:\\d+}")
    public User updateUser(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
        }

        log.info(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

        return user;
    }

    /**
     * 模拟数据库删除用户
     */
    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable("id") String id) {
        log.info(id);
    }
}
