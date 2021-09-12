package com.ironhack.banking_system.data.model;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    @Getter
    private String street;
    @Getter
    private String city;
    @Getter
    private String country;
    @Getter
    private String postalCode;

    public Address() {
    }

    public Address(String street, String city, String country, String postalCode) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }
}
