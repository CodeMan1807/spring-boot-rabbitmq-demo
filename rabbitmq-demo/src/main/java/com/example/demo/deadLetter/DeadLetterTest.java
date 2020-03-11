package com.example.demo.deadLetter;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DeadLetterTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/deadLetter-1")
    public String demo1() {
        // 工作队列1发送消息
        rabbitTemplate.convertAndSend(QueueConfig.WORK_EXCHANGE, "1", "我是消费者1");
        return "成功!";
    }

    @RequestMapping(value = "/deadLetter-2")
    public String demo2() {
        // 工作队列1发送消息
        rabbitTemplate.convertAndSend(QueueConfig.WORK_EXCHANGE, "1", "我是消费者1");
        return "成功!";
    }

    @RabbitListener(queues = QueueConfig.WORK_QUEUE_1)
    public void consumer1(String msg, Channel channel,Message message) {

        log.info("1收到消息:" + msg);
        System.out.println("消费者id-------->"+message.getMessageProperties().getMessageId());
//        try {
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @RabbitListener(queues = QueueConfig.WORK_QUEUE_2)
    public void consumer2(String msg) {

        log.info("2收到消息:" + msg);
//        int i = 1 / 0;
    }

//    @RabbitListener(queues = QueueConfig.DEAD_LETTER_QUEUE_1)
//    public void deadLetter(Message message) {
//        System.out.println("消费者id-------->"+message.getMessageProperties().getMessageId());
//        log.info("死信队列1收到：" + message.getMessageProperties());
//    }
//
//    @RabbitListener(queues = QueueConfig.DEAD_LETTER_QUEUE_2)
//    public void deadLetter2(Message message) {
//        log.info("死信队列2收到：" + message.getMessageProperties());
//    }

}
