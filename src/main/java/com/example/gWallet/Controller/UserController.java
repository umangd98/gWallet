package com.example.gWallet.Controller;

import com.example.gWallet.Model.Transaction;
import com.example.gWallet.Model.User;
import com.example.gWallet.Model.Wallet;
import com.example.gWallet.Repository.TransactionRepository;
import com.example.gWallet.Repository.UserRepository;
import com.example.gWallet.Repository.WalletDao;
import com.example.gWallet.Util.Producer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TransactionRepository trepository;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private WalletDao WDao;
    private static final String TOPIC = "test";
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    RedissonClient redisson = Redisson.create();
    @GetMapping("/users")
    List<User> findAll() {
        return repository.findAll();
    }

    // Save
    @PostMapping("/users")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    User newBook(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Find
    @GetMapping("/users/{id}")
    User findOne(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> {System.out.println("User Not Found");
                    return null;
                });
    }
    @PostMapping("/sendMoney")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    Transaction addBal(@RequestBody Transaction transaction) {
        transaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        int amt = transaction.getAmount();
        User sender = repository.findById(transaction.getSid())
                        .orElseThrow(() -> {System.out.println("User Not Found");
                            return null;
                        });
        User receiver = repository.findById(transaction.getRid())
                .orElseThrow(() -> {System.out.println("User Not Found");
                    return null;
                });
        if(sender.getBal()>=amt)
        {
            sender.setBal(sender.getBal()-amt);
            receiver.setBal(receiver.getBal()+amt);
            transaction.setStatus("SUCCESS");

        }
        else {
                transaction.setStatus("FAILED");
        }
        logger.info(String.format("$$ -> Producing Transaction --> %s",transaction));
        kafkaTemplate.send(TOPIC,Integer.toString(transaction.getAmount()));
        repository.save(receiver);
        repository.save(sender);
        return trepository.save(transaction);
    }
    @GetMapping("/getBal/{id}")
    Wallet getBal(@PathVariable int id)
    {
        return WDao.getWallet(id);
    }


}
