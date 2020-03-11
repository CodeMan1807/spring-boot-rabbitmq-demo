package com.example.demo.deadLetter;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.WildcardType;
import java.util.HashMap;

@Configuration
public class QueueConfig {

    public static final String WORK_QUEUE_1 = "WORK_QUEUE_1-1";

    public static final String WORK_QUEUE_2 = "WORK_QUEUE_1-2";

    public static String WORK_EXCHANGE = "WORK_EXCHANGE";



    @Bean
    public Queue workQueue1() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DeadLetterConfig.DEAD_LETTER_EXCHANGE);
        return new Queue(WORK_QUEUE_1, true, false, true, args);
    }

    @Bean
    public Queue workQueue2() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DeadLetterConfig.DEAD_LETTER_EXCHANGE);
        return new Queue(WORK_QUEUE_2, true, false, true, args);
    }


    @Bean
    public Exchange workExchange() {
        return ExchangeBuilder.directExchange(WORK_EXCHANGE).build();
    }


    @Bean
    public Binding work1Binding() {
        return BindingBuilder.bind(workQueue1()).to(workExchange()).with("1").noargs();
    }

    @Bean
    public Binding work2Binding() {
        return BindingBuilder.bind(workQueue2()).to(workExchange()).with("2").noargs();
    }



}
