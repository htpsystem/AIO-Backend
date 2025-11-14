package com.htpsystem.all_in_one_service_app.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private boolean isActive = true;
    @Column(name = "is_verified")
    private boolean isVerified = false;

    // --- RELATIONSHIPS ---
    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;


    // One-to-One relationship with user_data
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserData userData;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserAddress userAddress;
}