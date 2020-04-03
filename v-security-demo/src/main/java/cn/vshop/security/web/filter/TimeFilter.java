package cn.vshop.security.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/31 18:00
 */
// JEE 标准，默认不收spring管理
// 不要直接使用@Component，Spring无法管理，必须借助FilterRegistrationBean创建
//@Component
@Slf4j
public class TimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("time filter start");
        StopWatch watch = new StopWatch();
        watch.start("filter");
        filterChain.doFilter(servletRequest, servletResponse);
        watch.stop();
        System.out.println(watch.prettyPrint());
        log.info("time filter finish");
    }

    @Override
    public void destroy() {
        log.info("time filter destroy");
    }
}
