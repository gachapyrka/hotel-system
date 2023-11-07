package com.example.hotel.repo;

import com.example.hotel.domain.Role;
import com.example.hotel.domain.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserProfileRepo  extends CrudRepository<UserProfile, Long> {
    List<UserProfile> findByAccount_UsernameContainsOrCredentialsContainsOrTelephoneContainsOrPassportContains(String username,
                                                                                                               String credentials,
                                                                                                               String telephone,
                                                                                                               String passport);
}
