package com.example.hotel.domain;

import lombok.*;
import org.hibernate.Hibernate;

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
    @Column(length = 1000)
    private String description;

    @OneToMany (orphanRemoval = false, mappedBy = "hotel", fetch = FetchType.EAGER)
    private List<HotelImage> images;

    @OneToMany (orphanRemoval = false, mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<EmployeeProfile> employees;

    @OneToMany (orphanRemoval = false, mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<RoomType> roomTypes;
    @OneToMany (orphanRemoval = false, mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Feedback> comments;

    public Double getMinCost(){
        Hibernate.initialize(roomTypes);
        Double min = 0.;

        for(RoomType type: roomTypes){
            if(min == 0 || min > type.getCostPerDay())
                min = type.getCostPerDay();
        }

        return min;
    }

    public Double getMaxCost(){
        Hibernate.initialize(roomTypes);
        Double max = 0.;

        for(RoomType type: roomTypes){
            if(max < type.getCostPerDay())
                max = type.getCostPerDay();
        }

        return max;
    }

    public HotelImage getFirstImage(){
        Hibernate.initialize(images);

        if(!images.isEmpty())
            return images.get(0);

        return null;
    }

    public int getRoomsCount(){
        Hibernate.initialize(roomTypes);
        int count = 0;

        for(RoomType type: roomTypes){
            count += type.getRoomsCount();
        }

        return count;
    }

    public int getFreeRoomsCount(){
        Hibernate.initialize(roomTypes);
        int count = 0;

        for(RoomType type: roomTypes){
            count += type.getFreeRoomsCount();
        }

        return count;
    }
}
