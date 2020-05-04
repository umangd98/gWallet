package com.example.gWallet.Util;

import com.example.gWallet.Model.Transaction;
import com.example.gWallet.Model.User;
import com.example.gWallet.Repository.TransactionRepository;
import com.example.gWallet.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TransactionRepository trepository;
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @KafkaListener(topics = "test", groupId = "group_id")
    public void consume(String id){
        logger.info(String.format("$$ -> Consumed Message -> %s",id));
        Transaction t = trepository.findById(Integer.valueOf(id)).orElseThrow(() -> {System.out.println("User Not Found");
            return null;
        });
        User r = repository.findById(t.getRid()).orElseThrow(() -> {System.out.println("User Not Found");
            return null;
        });
        User s = repository.findById(t.getSid()).orElseThrow(() -> {System.out.println("User Not Found");
            return null;
        });
        String msg = "Amount Credited Rs."  + t.getAmount() + " from " + t.getSid() + " on " + t.getDate();
        String subject = "Transaction " + t.getId();
        SSLEmail.mail(r.getEmail(),msg,subject);
        msg = "Amount Debited Rs."  + t.getAmount() + " send to " + r.getName() + " on " + t.getDate()+" thanks, " + s.getName();
        subject = "Transaction " + t.getId();
        SSLEmail.mail(s.getEmail(),msg,subject);
    }
}
