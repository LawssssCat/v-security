package cn.vshop.security.web.async;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 模拟消息队列
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/2 1:16
 */
@Slf4j
@Getter
@Setter
@Component
public class MockQueue {

    /**
     * 代表下单消息
     */
    private String placeOrder;

    /**
     * 代表订单完成消息
     */
    private String completeOrder;

    /**
     * 模拟
     * 消息中间件到业务处理应用
     * 再到收到返回消息的过程
     */
    public void setPlaceOrder(String completeOrder) {

        // 新开一个线程，模拟被处理程序处理
        new Thread(() -> {
            log.info("接到下单请求：{}", completeOrder);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.completeOrder = completeOrder;
            log.info("下单请求处理完毕");
        }).start();
    }

}
