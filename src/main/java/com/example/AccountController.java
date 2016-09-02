package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Sergo on 21.08.2016.
 */

@CacheConfig(cacheNames = "amounts")
@RestController
@RequestMapping(value = "/service/{id}")
public class AccountController implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceApplication.class);

    @Autowired
    AccountRepository Repository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Account> getAll() {
        return Repository.findAll();
    }

    @RequestMapping(value = "/{amount}", method = RequestMethod.GET)
    public String adding(@PathVariable("id") Integer id, @PathVariable("amount") Long amount) {
        addAmount(id, amount);
        return "New Amount is " + amount + " for id=" + id + "!";
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getting(@PathVariable("id") Integer id) {

        Long amount;
        try {
            log.info("Start getting");
            amount = getAmount(id);
            log.info("Getting over");
        } catch (NullPointerException e) {
            return "Account with id " + id + " does not exist! Can't get amount! ";
        }

        return "YOUR Amount is " + amount + " with id=" + id + "!";
    }

    @Override
    @Cacheable(value = "amounts")
    public Long getAmount(Integer id) {
        Account temp = Repository.findById(id);
//        simulateSlowService();
        return temp.retAmount();
    }

    @Override
    @CachePut(value = "amounts")
    public void addAmount(Integer id, Long value) {
        Repository.save(new Account(id, value));
    }


    private void simulateSlowService() {
        try {
            long time = 5000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
