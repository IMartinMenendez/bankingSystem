package com.ironhack.banking_system.controllers.responses.users;

import com.ironhack.banking_system.data.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
@Setter
public class CreateAccountHolderResponse extends CreateUserResponse {
    @Getter
    private LocalDate dateOfBirth;
    @Getter
    private Address address;
    @Getter
    private String mailingAddress;

    public CreateAccountHolderResponse(long id, String name, Set<String> roles, LocalDate dateOfBirth, Address address, String mailingAddress) {
        super(id, name, roles);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.mailingAddress = mailingAddress;
    }
}
