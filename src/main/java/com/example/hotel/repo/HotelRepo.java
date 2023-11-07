package com.example.hotel.repo;

import com.example.hotel.domain.Hotel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelRepo extends CrudRepository<Hotel, Long> {
    List<Hotel> findByName(String name);
}
