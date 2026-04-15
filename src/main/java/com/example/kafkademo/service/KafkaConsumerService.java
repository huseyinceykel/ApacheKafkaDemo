package com.example.kafkademo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class KafkaConsumerService {
    private final Map<String, List<String>> messageStore = new ConcurrentHashMap<>();

    @KafkaListener(topics = "logistics-topic", groupId = "logistics-group")
    public void consume(ConsumerRecord<String, String> record) {
        String key = record.key() != null ? record.key() : "default-key";
        String message = record.value();

        messageStore.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>()))
                   .add(message);

        log.info("Key: {} | Partition: {} | Offset: {} | Mesaj: {}", 
                 key, record.partition(), record.offset(), message);
    }

    public List<String> getMessagesByKey(String key) {
        return messageStore.getOrDefault(key, Collections.emptyList());
    }

    public Map<String, List<String>> getAllMessages() {
        return messageStore;
    }
}
