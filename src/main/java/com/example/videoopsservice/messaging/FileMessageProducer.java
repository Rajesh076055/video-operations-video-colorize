package com.example.videoopsservice.messaging;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(FileMessage fileMessage) {
        rabbitTemplate.convertAndSend("originalFileQueue", fileMessage);
    }
}
