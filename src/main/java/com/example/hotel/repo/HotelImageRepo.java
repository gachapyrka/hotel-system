package com.example.hotel.repo;

import com.example.hotel.domain.HotelImage;
import org.springframework.data.repository.CrudRepository;

public interface HotelImageRepo extends CrudRepository<HotelImage, Long> {
}
