package cn.vshop.security.web.config;

import cn.vshop.security.web.filter.TimeFilter;
import cn.vshop.security.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.util.ArrayList;

/**
 * 把第三方的filter加入到spring的容器中
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/3/31 18:06
 */
@Configuration
// 继承 WebMvc注册类的适配:WebMvcConfigurerAdapter
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TimeInterceptor timeInterceptor;

    // 将拦截器注册进springmvc
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor).addPathPatterns("/filter");
    }

    /**
     * 使用 FilterRegistrationBean 而不是直接 @Component 一个Filter的好处是：
     * + 可以通过setUrlPatterns，指定哪些路径通过拦截器，哪些路径不通过
     */
    @Bean
    public FilterRegistrationBean timeFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        Filter filter = new TimeFilter();
        registrationBean.setFilter(filter);

        ArrayList<String> urls = new ArrayList<>();
        urls.add("/filter");
        registrationBean.setUrlPatterns(urls);

        return registrationBean;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 选择1 用 Callable 做异步
        // configurer.registerCallableInterceptors();
        // 选择2 用 DeferredResult 做异步
        // configurer.registerDeferredResultInterceptors()

        // 设置超时时间
        // configurer.setDefaultTimeout()

        // 用于自定义线程池
        // configurer.setTaskExecutor()
    }
}
