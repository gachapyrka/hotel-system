package com.example.hotel.repo;

import com.example.hotel.domain.Account;
import com.example.hotel.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long> {
    Account findByUsername(String username);

    Iterable<Account> findByRole(Role role);
}
