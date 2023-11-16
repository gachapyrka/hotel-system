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
@Table(name = "Room")
public class Room {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Номер комнаты не может быть пустым")
    private String number;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="room_type_id")
    private RoomType roomType;
    @OneToMany (mappedBy = "room", fetch = FetchType.EAGER)
    private List<BorrowedRoom> borrowedRecords;

    public boolean isLocked(){
        boolean isLocked = false;
        for(BorrowedRoom rec:borrowedRecords){
            if(rec.getStatus() == Status.ACTIVE){
                isLocked = true;
                break;
            }
        }

        return isLocked;
    }
}
