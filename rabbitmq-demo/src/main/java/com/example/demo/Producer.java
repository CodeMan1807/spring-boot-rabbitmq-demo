package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "/demo")
    public String demo(@RequestParam(required = false) Boolean flag) {
        if (flag == null) {
            flag = false;
        }
        rabbitTemplate.convertAndSend(BroadCast.EXCHANGE_NAME, BroadCast.ROUTING_KEY_NAME, flag);
        return "发送成功!";
    }

    @GetMapping(value = "demo2")
    public String demo2() {
        MQCorrelationData mqCorrelationData = new MQCorrelationData(UUID.randomUUID().toString());
        // 设置发送的消息
//        List<String> strings = Arrays.asList("小猫", "小于", "小狗");
//        mqCorrelationData.setMessage(strings.toString());
        // 设置交换机
        mqCorrelationData.setExchange("exchange-rabbit-springboot-advance");
        // 设置路由键
        mqCorrelationData.setRoutingKey("ttl");

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageProperties.DEFAULT_DELIVERY_MODE);
//        Message message = new Message(strings.toString().getBytes(), messageProperties);

        this.rabbitTemplate.convertAndSend("exchange-rabbit-springboot-advance", "product11", "xxx", mqCorrelationData);
        return "发送成功!";
//        String exchange = "exchange-rabbit-springboot-advance";
//        String routingKey = "product";
//        String unRoutingKey = "norProduct";
//
//
//        // 1.发送一条正常的消息 CorrelationData唯一（可以在ConfirmListener中确认消息）
//        IntStream.rangeClosed(0, 1).forEach(num -> {
//            String message = LocalDateTime.now().toString() + "发送第" + (num + 1) + "条消息.";
//            rabbitTemplate.convertAndSend(exchange, "aaa", message, new CorrelationData("routing" + UUID.randomUUID().toString()));
//            log.info("发送一条消息,exchange:[{}],routingKey:[{}],message:[{}]", exchange, routingKey, message);
//        });
//        // 2.发送一条未被路由的消息，此消息将会进入备份交换器（alternate exchange）
//        String message = LocalDateTime.now().toString() + "发送没有正确路由的1条消息.";
//        rabbitTemplate.convertAndSend(exchange, unRoutingKey, message, new CorrelationData("unRouting-" + UUID.randomUUID().toString()));
//        log.info("发送错误路由的一条消息,exchange:[{}],routingKey:[{}],message:[{}]", exchange, unRoutingKey, message);
    }

}
