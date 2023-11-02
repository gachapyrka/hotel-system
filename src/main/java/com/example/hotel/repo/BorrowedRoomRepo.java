package com.example.hotel.repo;

import com.example.hotel.domain.BorrowedRoom;
import org.springframework.data.repository.CrudRepository;

public interface BorrowedRoomRepo extends CrudRepository<BorrowedRoom, Long> {
}
