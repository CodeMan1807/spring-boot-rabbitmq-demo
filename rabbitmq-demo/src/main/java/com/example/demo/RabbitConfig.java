package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.DirectRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@Slf4j
public class RabbitConfig {

    private Long retryCount = 3L;


    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());

        // 开启事务
//        rabbitTemplate.setChannelTransacted(true);

        // 消息是否成功发送到Exchange
//        RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
//            if (ack) {
//                log.error("消息成功发送到Exchange");
//                // 进行重发
//                if (correlationData instanceof MQCorrelationData) {
////                    MQCorrelationData mqCorrelationData = (MQCorrelationData) correlationData;
////                    Object message = mqCorrelationData.getMessage();
////                    String exchange = mqCorrelationData.getExchange();
////                    String routingKey = mqCorrelationData.getRoutingKey();
////                    int retryNum = mqCorrelationData.getRetryCount();
////                    if (retryNum < retryCount){
////                        rabbitTemplate.convertAndSend(exchange,routingKey,message,correlationData);
////                        mqCorrelationData.setRetryCount(retryNum+1);
////                    }
//                }
//            } else {
//                log.error("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
//            }
//        };
//        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
//        rabbitTemplate.setMandatory(true);
        // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
        // message消息对象可以获取消息内容，exchange,routingkey等信息
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}", exchange, routingKey, replyCode, replyText, message);
        });

        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(value = "directRabbitListenerContainerFactory")
    public RabbitListenerContainerFactory rabbitListenerContainerFactory( DirectRabbitListenerContainerFactoryConfigurer configurer){
        DirectRabbitListenerContainerFactory directRabbitListenerContainerFactory = new DirectRabbitListenerContainerFactory();
        configurer.configure(directRabbitListenerContainerFactory,connectionFactory);
        return directRabbitListenerContainerFactory;
    }

    @Bean("rabbitTransactionManager")
    public RabbitTransactionManager rabbitTransactionManager(CachingConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

}
