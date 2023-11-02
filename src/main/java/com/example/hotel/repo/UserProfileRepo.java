package com.example.hotel.repo;

import com.example.hotel.domain.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepo  extends CrudRepository<UserProfile, Long> {
}
