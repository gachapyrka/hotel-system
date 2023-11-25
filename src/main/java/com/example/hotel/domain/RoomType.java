package com.example.hotel.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

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
    @Column(length = 1000)
    @Length(max = 1000)
    private String description;
    @PositiveOrZero(message = "Стоимость не может быть отрицательной")
    private Double costPerDay;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_id")
    private Hotel hotel;
    @OneToMany (orphanRemoval = true, mappedBy = "roomType", fetch = FetchType.EAGER)
    private List<RoomImage> roomImages;
    @OneToMany (orphanRemoval = true, mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<Room> rooms;
    @OneToMany (orphanRemoval = true, mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<BorrowRequest> requests;

    public int getRoomsCount(){
        return rooms.size();
    }

    public int getFreeRoomsCount(){
        int result = 0;

        for(Room room: rooms){
            Hibernate.initialize(room.getBorrowedRecords());

            boolean isBorrowed = false;
            for(BorrowedRoom borrowedRoom : room.getBorrowedRecords()){
                if(borrowedRoom.getStatus() == Status.ACTIVE){
                    isBorrowed = true;
                    break;
                }
            }

            if(!isBorrowed){
                result++;
            }
        }

        return result;
    }
}
