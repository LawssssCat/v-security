package cn.vshop.security.web.async;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/1 22:54
 */
@RestController
public class AsyncController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 模拟的消息队列
     */
    @Autowired
    private MockQueue mockQueue;

    /**
     * 请求响应应用的结果存储器
     */
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @RequestMapping("/order")
    public DeferredResult<String> order() throws InterruptedException {
        logger.info("主线程开始");

        /* 注释掉：用消息队列代替

        // 单开一个线程，处理业务逻辑
        // 这个线程在Spring环境下运行
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                logger.info("@ @ 副线程开始");
                // 模拟业务逻辑的处理
                Thread.sleep(1000);
                logger.info("@ @ 副线程结束");
                return "success";
            }
        };

        */

        // 生成一个8为随机数作为订单号
        String orderNumber = RandomStringUtils.randomNumeric(8);
        // 订单号放入消息队列
        mockQueue.setPlaceOrder(orderNumber);
        // 延迟(Deferred)处理结果
        DeferredResult<String> result = new DeferredResult<>();
        // 订单号和处理结果一同放进结果处理器的map中
        deferredResultHolder.getMap().put(orderNumber, result);

        logger.info("主线程返回");
        return result;
    }

}
