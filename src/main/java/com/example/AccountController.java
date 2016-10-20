package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

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

    private ConcurrentMap<Integer, Integer> accountIds = new ConcurrentHashMap<>();

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Account> getAll() {
        return Repository.findAll();
    }

    @RequestMapping(value = "/{value}", method = RequestMethod.GET)
    public String adding(@PathVariable("id") Integer id, @PathVariable("value") Long value) {
            addAmount(id, value);
            return "New Amount is " + getAmount(id) + " for id=" + id + "!";
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
        return Repository.findById(id).retAmount();
    }

    @Override
    @CachePut(value = "amounts")
    @Transactional
    public void addAmount(Integer id, Long value) {
        if (accountIds.putIfAbsent(id, id) != null)
        {
            throw new ConcurrentModificationException();
        }

        try {
            Repository.save(new Account(id, getAmount(id) + value));
        } catch (NullPointerException e) {
            Repository.save(new Account(id, value));
        }

        // говорим, что можно снова обращаться к этому аккаунту
        accountIds.remove(id);
    }

}
