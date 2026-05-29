package org.example.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long addressId;
    private String buildingName;
    private String city;
    private String country;
    private String pinCode;
    private String state;
    private String street;
}