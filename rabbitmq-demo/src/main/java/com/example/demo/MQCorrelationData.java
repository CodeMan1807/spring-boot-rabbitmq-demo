package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.util.UUID;

@Getter
@Setter
public class MQCorrelationData extends CorrelationData {


    //消息体
    private volatile Object message;
    //交换机
    private String exchange;
    //路由键
    private String routingKey;
    //重试次数
    private int retryCount = 0;

    public MQCorrelationData(String id) {
        super(id);
    }
}
