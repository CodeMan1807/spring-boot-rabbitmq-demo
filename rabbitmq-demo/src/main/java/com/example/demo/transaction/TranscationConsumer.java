package com.example.demo.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TranscationConsumer {

    @RabbitListener(queuesToDeclare = @Queue(name = "transcaction.test",durable = "true"))
    public void reveive(String msg){
        log.info("消费者收到消息——---》",msg);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "direct.queue1", durable = "true"),
                    exchange = @Exchange(value = "spring.direct.exchange",type = "direct"),
                    declare = "true",
                    key = "test"
            ))
    public void demo1(){
        log.info("消费者1收到了消息");
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "direct.queue1", durable = "true"),
                    exchange = @Exchange(value = "spring.direct.exchange",type = "direct"),
                    declare = "true",
                    key = "test"
            ))
    public void demo2(){
        log.info("消费者2收到了消息");
    }


}
