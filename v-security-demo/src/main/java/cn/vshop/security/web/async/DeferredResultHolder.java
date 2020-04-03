package cn.vshop.security.web.async;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求处理应用(tomcat)在请求和响应中传递 DefferredResult 的订单存储器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/2 1:22
 */
@Component
@Getter
@Setter
public class DeferredResultHolder {

    /**
     * key 订单号
     * DeferredResult<String> 订单处理结果
     */
    private Map<String, DeferredResult<String>> map = new HashMap<>();

}
