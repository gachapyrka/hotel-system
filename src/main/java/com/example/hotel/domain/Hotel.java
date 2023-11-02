package com.example.hotel.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Hotel")
public class Hotel {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Название отеля не может быть пустым")
    private String name;
    private String description;

    @OneToMany (mappedBy = "hotel", fetch = FetchType.EAGER)
    private List<HotelImage> images;

    @OneToMany (mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<EmployeeProfile> employees;

    @OneToMany (mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<RoomType> roomTypes;
    @OneToMany (mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Feedback> comments;
}
