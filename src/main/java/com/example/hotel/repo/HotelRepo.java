package com.example.hotel.repo;

import com.example.hotel.domain.Hotel;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepo extends CrudRepository<Hotel, Long> {
}
