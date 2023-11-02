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
@Table(name = "EmployeeProfile")
public class EmployeeProfile {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "ФИО не может быть пустым")
    private String credentials;

    @NotBlank(message = "Телефон не может быть пустым")
    private String telephone;

    @OneToOne(optional = false, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn (name="account_id")
    private Account account;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="hotel_id")
    private Hotel hotel;
}
