package com.htpsystem.all_in_one_service_app.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {

    // ---- User ----
    private String email;
    private String password;
    private String role;  // references roles table

    // ---- User Data ----
    private String firstName;
    private String lastName;
    private String dob;
    private String gender; // references gender table

    // ---- Address ----
    private String country;
    private String city;
    private String pincode;
    private String district;
    private String state;
    private String buildingName;
    private String street;
    private String landmark;
}
