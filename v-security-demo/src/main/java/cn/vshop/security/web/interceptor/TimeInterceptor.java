package cn.vshop.security.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/31 19:21
 */
@Slf4j
// spring 提供的类，默认被spring管理
@Component
public class TimeInterceptor implements HandlerInterceptor {


    // 控制器(controller)方法处理前调用
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("@@@@ interceptor preHandle");
        long start = System.currentTimeMillis();
        // 为了在方法间传递信息，类似可用ThreadLocal
        httpServletRequest.setAttribute("start", start);
        // 返回false将不继续执行下面方法
        return true;
    }

    // 控制器(controller)方法处理后调用
    // 但是，如果controler执行期间出现异常，postHandle将不被调用
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("@@@@ interceptor postHandle");
        long start = (long) httpServletRequest.getAttribute("start");
        log.info("@@@@ 用时:{}", System.currentTimeMillis() - start);
    }

    // postHandle方法处理后调用
    // 无论，controler执行期间是否出现异常，afterCompletion都将被调用
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("@@@@ interceptor afterCompletion");
        long start = (long) httpServletRequest.getAttribute("start");
        log.info("@@@@ 用时:{}", System.currentTimeMillis() - start);
        log.info("@@@@ exception:{}", e);
    }
}
