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
@Table(name = "UserProfile")
public class UserProfile {

    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "ФИО не может быть пустым")
    private String credentials;
    @NotBlank(message = "Паспортные данные не могут быть пустым")
    private String passport;

    @NotBlank(message = "Телефон не может быть пустым")
    private String telephone;

    @OneToOne(optional = false, cascade=CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn (name="account_id")
    private Account account;

    @OneToMany (orphanRemoval = false, mappedBy = "userProfile", fetch = FetchType.EAGER)
    private List<BorrowRequest> requests;
    @OneToMany (orphanRemoval = false, mappedBy = "userProfile", fetch = FetchType.LAZY)
    private List<BorrowedRoom> borrowedRecords;
    @OneToMany (orphanRemoval = false, mappedBy = "userProfile", fetch = FetchType.LAZY)
    private List<Feedback> comments;
}
