package com.example.hotel.repo;

import com.example.hotel.domain.EmployeeProfile;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeProfileRepo extends CrudRepository<EmployeeProfile, Long> {
}
