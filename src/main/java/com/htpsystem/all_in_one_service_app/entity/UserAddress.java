package com.htpsystem.all_in_one_service_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserAddress")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String pincode;
    private String district;
    private String state;
    private String buildingName;
    private String street;
    private String landmark;

    @OneToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;

}
