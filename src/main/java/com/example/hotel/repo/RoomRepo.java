package com.example.hotel.repo;

import com.example.hotel.domain.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepo extends CrudRepository<Room, Long> {
    Room findByNumber(String number);
    List<Room> findByRoomType_Id(long id);
}
