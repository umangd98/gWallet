package com.example.gWallet.Util;

import com.example.gWallet.Model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @KafkaListener(topics = "test", groupId = "group_id")
    public void consume(String id){
        logger.info(String.format("$$ -> Consumed Message -> %s",id));
    }
}
