package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
<<<<<<< HEAD

=======
>>>>>>> 1990e186dc0fda0f454dc606009a90e57794f98d
    private static final Logger log = LoggerFactory.getLogger(AccountServiceApplication.class);

    @Autowired
    AccountRepository Repository;

    private HashSet<Integer> myHashSet = new HashSet<Integer>();

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Account> getAll() {
        return Repository.findAll();
    }

    @RequestMapping(value = "/{value}", method = RequestMethod.GET)
    public String adding(@PathVariable("id") Integer id, @PathVariable("value") Long value) {
        if (myHashSet.add(id)) {
            addAmount(id, value);
            return "New Amount is " + getAmount(id) + " for id=" + id + "!";
        }
        else return "Try later!";
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
<<<<<<< HEAD
    @Transactional
=======
>>>>>>> 1990e186dc0fda0f454dc606009a90e57794f98d
    public void addAmount(Integer id, Long value) {
        try {
            Repository.save(new Account(id, getAmount(id) + value));
            myHashSet.remove(id);
        } catch (NullPointerException e) {
            Repository.save(new Account(id, value));
            myHashSet.remove(id);
        }
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
