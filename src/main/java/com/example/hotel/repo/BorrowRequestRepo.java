package com.example.hotel.repo;

import com.example.hotel.domain.BorrowRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BorrowRequestRepo extends CrudRepository<BorrowRequest, Long> {
    List<BorrowRequest> findByDescriptionContainsOrRoomType_NameContainsOrRoomType_Hotel_NameContains(String description, String name, String hotel);
    List<BorrowRequest> findByRoomType_Hotel_Id(long id);
}
