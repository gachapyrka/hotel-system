package com.example.hotel.repo;

import com.example.hotel.domain.EmployeeProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeProfileRepo extends CrudRepository<EmployeeProfile, Long> {
    EmployeeProfile findByAccount_Id(Long accountId);
    List<EmployeeProfile> findByAccount_UsernameContainsOrCredentialsContainsOrTelephoneContainsOrHotel_NameContains(String username,
                                                                                                               String credentials,
                                                                                                               String telephone,
                                                                                                               String hotelName);
}
