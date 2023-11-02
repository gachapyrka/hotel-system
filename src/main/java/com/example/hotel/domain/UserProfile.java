package com.example.hotel.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @OneToOne(optional = false, cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn (name="account_id")
    private Account account;
}
