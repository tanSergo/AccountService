package com.example;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.*;


/**
 * Created by Sergo on 21.08.2016.
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private Integer id;

    private Long amount;

    protected Account() {}

    public Account(Integer id, Long amount) {
        this.id = id;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public Account setId(Integer id) {
        this.id = id;
        return this;
    }

    public Long retAmount() {
        return amount;
    }

}
