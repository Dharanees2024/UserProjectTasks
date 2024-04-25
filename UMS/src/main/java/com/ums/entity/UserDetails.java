package com.ums.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="excel")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String companyId;


    // Constructors, getters, and setters
}
