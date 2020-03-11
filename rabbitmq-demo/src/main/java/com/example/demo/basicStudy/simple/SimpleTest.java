package com.example.demo.basicStudy.simple;


import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "simple")
public class SimpleTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "demo")
    public void demo(){

        String msg = "这是一个简单队列模式";
        // 绑定到默认交换机
        rabbitTemplate.convertAndSend("spring.simple.queue", msg );
    }

    @Component
    public class SimpleListener{

        // 通过注解声明队列
        @RabbitListener(queuesToDeclare = @Queue("spring.simple.queue"))
        public void listen(String message){
            System.out.println("简单队列收到消息---------->");
        }
    }

}
