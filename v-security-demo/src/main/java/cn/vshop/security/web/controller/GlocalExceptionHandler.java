package cn.vshop.security.web.controller;

import cn.vshop.security.exception.ServiceException;
import cn.vshop.security.exception.UserNotExistException;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * "@ControllerAdvice" 是Controller的增强
 * 用来对:
 * 1.@ExceptionHandler 异常处理
 * 2.@InitBinder 用于初始化 WebDataBinder(表单数据绑定)
 * 3.@ModelAttribute 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行
 * <p>
 * 这些注解进行处理，可以跨域多个 controller类
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/3/31 14:58
 */
@ControllerAdvice
@Controller // 使ErrorController接口生效，从而让 404 错误被我们捕获
public class GlocalExceptionHandler implements ErrorController {

    private final static String PAGE_NOT_FOUND = "/error";

    @Override
    public String getErrorPath() {
        return PAGE_NOT_FOUND;
    }

    @RequestMapping(PAGE_NOT_FOUND)
    public void toCustomHandler() {
        throw new ServiceException("Page not found");
    }

    /**
     * 拦截并处理 ServiceException 及其子类异常
     *
     * @param se 拦截到的异常
     * @return map类型数据，供jackson解析成json格式
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    // 响应码设置为 400(bad request)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleServiceException(ServiceException se) {
        HashMap<String, Object> result = new HashMap<>();

        if (se instanceof UserNotExistException) {
            // UserNotExistException
            result.put("id", ((UserNotExistException) se).getId());
        } else {
            // ServerException
        }
        result.put("message", se.getMessage());
        return result;
    }

}
