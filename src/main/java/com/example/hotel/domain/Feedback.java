package com.example.hotel.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Feedback")
public class Feedback {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id")
    private UserProfile userProfile;
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_id")
    private Hotel hotel;
    private Date creationDate;
    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;
    @NotBlank(message = "Текст не может быть пустым")
    private String text;

}
