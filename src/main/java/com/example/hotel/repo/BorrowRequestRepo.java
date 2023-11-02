package com.example.hotel.repo;

import com.example.hotel.domain.BorrowRequest;
import org.springframework.data.repository.CrudRepository;

public interface BorrowRequestRepo extends CrudRepository<BorrowRequest, Long> {
}
