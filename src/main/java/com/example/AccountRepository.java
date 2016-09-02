package com.example;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


/**
 * Created by Sergo on 21.08.2016.
 */

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(Integer id);
}
