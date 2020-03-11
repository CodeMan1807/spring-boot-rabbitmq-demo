package com.example.demo.basicStudy.work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "work")
public class WorkTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "demo")
    public void demo() throws Exception{

        String msg = "这是一个work模式";
        // 绑定到默认交换机
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("spring.work.queue",msg+i);
        }
//        Thread.sleep(5000);

    }

    @Component
    public class WorkListener{

        // 通过注解声明队列
        @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
        public void listen(String msg){
            System.out.println("work模式 接收到消息：" + msg);
        }

        @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
        public void listen2(String msg) {
            System.out.println("work模式二 接收到消息：" + msg);
        }

    }

}
