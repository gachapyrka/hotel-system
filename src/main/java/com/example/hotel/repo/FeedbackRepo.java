package com.example.hotel.repo;

import com.example.hotel.domain.Feedback;
import org.springframework.data.repository.CrudRepository;

public interface FeedbackRepo extends CrudRepository<Feedback, Long> {
}
