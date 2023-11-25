package com.example.hotel.domain;

import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "BorrowedRoom")
public class BorrowedRoom {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id")
    private UserProfile userProfile;
    private Date startDate;
    private Date endDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    public String getLocalStartDate(){
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        return fmt.format(startDate);
    }

    public String getLocalEndDate(){
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        return fmt.format(endDate);
    }

    public boolean isCancelled(){
        return status == Status.CANCELLED;
    }
}
