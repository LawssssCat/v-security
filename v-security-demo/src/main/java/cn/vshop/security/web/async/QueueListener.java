package cn.vshop.security.web.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 请求响应进程的消息队列监听器，
 * 当监听到消息队列中有处理好的结果，便把结果封装交给SpringMVC，最终返回给前端
 * <p>
 * ApplicationListener 是 spring 上下文(context)的监听器接口
 * ContextRefreshedEvent 容器初始化完毕的事件，监听这个事件相当于指定当程序启动起来后，我们需要做的事情
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/2 10:43
 */
@Slf4j
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    // 消息队列(自定义的、模拟的)
    @Autowired
    private MockQueue mockQueue;

    // 请求和响应间的订单存储器
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    /**
     * 事件触发后执行代码
     *
     * @param event 事件，这里是 ContextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // 因为是死循环，因此必须新开一个线程，否则会阻塞程序的启动
        new Thread(() -> {
            while (true) {
                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {
                    // 如果订单完成字段有值，那么就需要做出一些处理
                    String orderNumber = mockQueue.getCompleteOrder();
                    log.info("返回订单处理结果：{}", orderNumber);
                    // 模拟处理的结果为 "place order success"
                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success");

                    mockQueue.setCompleteOrder(null);
                } else {
                    // 如果没有，就等待一段时间，再查看
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }
}
