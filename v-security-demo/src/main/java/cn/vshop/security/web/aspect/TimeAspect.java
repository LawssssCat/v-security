package cn.vshop.security.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/1 0:38
 */
@Slf4j
// 定义为切面
@Aspect
// 让spring容器管理
@Component
public class TimeAspect {

    //  @Around 包围方式调用切点
    // 切入点表达式: UserController的任何返回值的任何参数的任何方法
    @Around("execution(* cn.vshop.security.web.controller.FilterController.*(..))")
    public Object handControllerMethod(
            // 当前切点
            ProceedingJoinPoint pj
    ) throws Throwable {

        log.info("### ### time aspect start");

        long start = new Date().getTime();

        // aspect相比于interceptor的优点，能获取参数
        Object[] args = pj.getArgs();
        for (Object arg : args) {
            log.info("### ### arg is {}", arg);
        }

        Object result = pj.proceed();

        long end = new Date().getTime();
        log.info("### ### time aspect 耗时:{}", (end - start));

        log.info("### ### time aspect end");

        return result;

    }

}
