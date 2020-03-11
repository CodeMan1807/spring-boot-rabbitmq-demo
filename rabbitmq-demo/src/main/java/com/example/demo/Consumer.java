package com.example.demo;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
@Slf4j
public class Consumer {


//    @RabbitListener(queues = BroadCast.QUEUE_NAME)
//    public void consume(Boolean flag,Message message, Channel channel) throws IOException {
//        log.error("收到消息: {}",flag );
//
//        MessageProperties properties = message.getMessageProperties();
//        long tag = properties.getDeliveryTag();
//
//        boolean success = flag;
//        if (success) {
//            channel.basicAck(tag, false);// 消费确认
//            log.error("手动确认:收到-------->");
//        } else {
//            channel.basicNack(tag, false, true);
//            log.error("手动确认:没有收到-------->");
//
//        }
//    }

    /**
     * 监听 queue-rabbit-springboot-advance 队列
     *
     * @param receiveMessage 接收到的消息
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "queue-rabbit-springboot-advance")
    public void receiveMessage(String receiveMessage, Message message, Channel channel) throws Exception {
//        try {

        log.error("收到消息");
//        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        int i = 1 / 0;
        // 手动签收
//            log.error("消费者接收到消息:[{}]", receiveMessage);
        if (new Random().nextInt(10) < 5) {
            log.warn("消费者拒绝确认信息:[{}]，此消息将会由死信交换器进行路由.", receiveMessage);
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } else {
            log.error("消费者确认收到:[{}]", receiveMessage);
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

//        catch (Exception e) {
//            log.info("接收到消息之后的处理发生异常.", e);
//            try {
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            } catch (IOException e1) {
//                log.error("签收异常.", e1);
//            }
//        }
//    }


    /**
     * 死信队列监听
     */
    @RabbitListener(queues = "dlx-queue")
    public void dlxQueue(String receiveMessage, Message message, Channel channel) {
        log.error("死信队列收到信息:{}", receiveMessage);

        // 手动签收
        try {
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 错误路由的队列监听
     */
    @RabbitListener(queues = "queue-unroute")
    public void unrouteQueue(String receiveMessage, Message message, Channel channel){
        log.error("错误路由队列收到信息:{}",receiveMessage);

        // 手动签收
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RabbitListener(queues = "ttl.queue")
    public void ttlQueue(Object receiveMessage, Message message, Channel channel) throws IOException {
        log.error("ttl队列收到信息:{}", receiveMessage);
        int i = 1 / 0;
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }

}
