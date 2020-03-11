package com.example.demo.basicStudy.fanout;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fanout")
public class FanoutTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "demo")
    public void demo() throws Exception {

        String msg = "订阅模式";
        for (int i = 0; i < 10; i++) {
            // 这里注意细节，第二个参数需要写，否则第一个参数就变成routingKey了
            rabbitTemplate.convertAndSend("spring.fanout.exchange", "aaa",msg + i);
        }
//        Thread.sleep(5000);
    }

    @Component
    public class FanoutListener {

        // 通过注解声明队列
        @RabbitListener(bindings = @QueueBinding(
                value = @Queue(value = "spring.fanout.queue", durable = "true"),
                exchange = @Exchange(
                        value = "spring.fanout.exchange",
                        ignoreDeclarationExceptions = "true",
                        type = ExchangeTypes.FANOUT
                ),
                key = "aa"

        )
        )
        public void listen(String msg) {
            System.out.println("订阅模式1 接收到消息：" + msg);
        }

        // 通过注解声明队列
        @RabbitListener(bindings = @QueueBinding(
                value = @Queue(value = "spring.fanout.queue2", durable = "true"),
                exchange = @Exchange(
                        value = "spring.fanout.exchange",
                        ignoreDeclarationExceptions = "true",
                        type = ExchangeTypes.FANOUT
                ),
                key = "bb"
        )
        )
        public void listen2(String msg) {
            System.out.println("订阅模式2 接收到消息：" + msg);
        }

    }


}
