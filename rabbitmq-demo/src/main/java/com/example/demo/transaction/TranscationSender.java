package com.example.demo.transaction;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@Slf4j
public class TranscationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 使用外部事务，实现全部成功后才投递消息
//    @Transactional(rollbackFor = Exception.class, transactionManager = "rabbitTransactionManager")
    @Transactional(rollbackFor = Exception.class)
    public String sendMessage(String msg) {

        rabbitTemplate.convertAndSend("spring.direct.exchange","test", msg);
        log.info("发送成功------->", msg);
        // 出现异常时，消息不会发送到mq中去
        int i = 1 / 0;
        return msg;
    }


}
