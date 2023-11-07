package com.example.hotel.repo;

import com.example.hotel.domain.RegistrationKey;
import com.example.hotel.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistrationKeyRepo extends CrudRepository<RegistrationKey, Long> {
    List<RegistrationKey> findByUsername(String username);
    List<RegistrationKey> findByRoleAndIsReferal(Role role, boolean isReferal);
    List<RegistrationKey> findByUsernameContainingAndRoleAndIsReferal(String infix, Role role, boolean isReferal);
}
