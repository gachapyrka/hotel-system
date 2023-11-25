package com.example.hotel.repo;

import com.example.hotel.domain.BorrowRequest;
import com.example.hotel.domain.BorrowedRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BorrowedRoomRepo extends CrudRepository<BorrowedRoom, Long> {
    List<BorrowedRoom> findByRoom_RoomType_Hotel_Id(long id);
    List<BorrowedRoom> findByUserProfile_Account_Id(long id);
}
