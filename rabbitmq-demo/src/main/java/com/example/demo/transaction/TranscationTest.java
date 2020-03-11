package com.example.demo.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranscationTest {

    @Autowired
    private TranscationSender sender;

    @RequestMapping(value = "transcation",method = RequestMethod.GET)
    public String test(){
        String s = sender.sendMessage("Hello world!");
        return s;
    }


}
