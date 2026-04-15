package com.example.kafkademo.controller;

import com.example.kafkademo.service.KafkaConsumerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaProducerController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConsumerService kafkaConsumerService;

    public KafkaProducerController(KafkaTemplate<String, String> kafkaTemplate, KafkaConsumerService kafkaConsumerService) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @PostMapping("/publish")
    public String sendMessage(@RequestParam(defaultValue = "default-key") String key, @RequestParam String message) {
        kafkaTemplate.send("logistics-topic", key, message);
        return "Gönderildi! Key: " + key + " | Mesaj: " + message;
    }

    @GetMapping("/messages")
    public List<String> getMessages(@RequestParam(defaultValue = "default-key") String key) {
        return kafkaConsumerService.getMessagesByKey(key);
    }

    @GetMapping("/messages/all")
    public Map<String, List<String>> getAllMessages() {
        return kafkaConsumerService.getAllMessages();
    }
}
