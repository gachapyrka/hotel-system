package com.example.hotel.repo;

import com.example.hotel.domain.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepo extends CrudRepository<Room, Long> {
}
