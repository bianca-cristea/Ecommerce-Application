package org.example.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Building name must have at least 4 characters")
    @Column(name = "building_name")
    private String buildingName;

    private String city;

    private String country;

    private String pinCode;

    private String state;

    private String street;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String buildingName, String city, String country, String pinCode, String state, String street) {
        this.buildingName = buildingName;
        this.city = city;
        this.country = country;
        this.pinCode = pinCode;
        this.state = state;
        this.street = street;
    }
}
