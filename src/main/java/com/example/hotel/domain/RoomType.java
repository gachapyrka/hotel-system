package com.example.hotel.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "RoomType")
public class RoomType {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Название типа комнаты не может быть пустым")
    private String name;
    private String description;
    @PositiveOrZero(message = "Стоимость не может быть отрицательной")
    private Double costPerDay;
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_id")
    private Hotel hotel;
    @OneToMany (mappedBy = "roomType", fetch = FetchType.EAGER)
    private List<RoomImage> roomImages;
    @OneToMany (mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<Room> rooms;
    @OneToMany (mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<BorrowRequest> requests;
}
