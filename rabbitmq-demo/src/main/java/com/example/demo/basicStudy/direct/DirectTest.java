package com.example.demo.basicStudy.direct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "direct")
public class DirectTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private RabbitListenerContainerFactory factory;

    @RequestMapping(value = "demo")
    public void demo() {
        String msg = "路由模式";
//        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("spring.direct.exchange", "bbb", msg );
//        }
    }

    @Component
    public class DirectListener {

        @RabbitListener(
                bindings = @QueueBinding(
                        value = @Queue(value = "spring.direct.queue", durable = "true"),
                        exchange = @Exchange(value = "spring.direct.exchange"),
                        declare = "true",
                        key = "aaa"
                ))
        public void listen(String msg, Message message) {
            int i = 1 / 0;
//            MessageListenerContainer listenerContainer = factory.createListenerContainer();
            System.out.println("路由模式1收到消息->" + msg);
        }

        @RabbitListener(containerFactory ="directRabbitListenerContainerFactory",
                bindings = @QueueBinding(
                        value = @Queue(value = "spring.direct3.queue", durable = "true"),
                        exchange = @Exchange(value = "spring.direct.exchange"),
                        declare = "true",
                        key = "bbb"
                ))
        public void listen2(String msg, Message message) {
            int i = 1 / 0;
//            MessageListenerContainer listenerContainer = factory.createListenerContainer();
            System.out.println("路由模式2收到消息->" + msg);
        }

    }


}
