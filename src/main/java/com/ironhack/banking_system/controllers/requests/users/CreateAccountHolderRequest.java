package com.ironhack.banking_system.controllers.requests.users;

import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.Role;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateAccountHolderRequest extends CreateUserRequest{
    @Getter
    private LocalDate dateOfBirth;
    @Getter
    private Address address;
    @Getter
    private String mailingAddress;
    @Getter
    private String password;

    public CreateAccountHolderRequest() {
    }

    public CreateAccountHolderRequest(String name, Set<String> roles, LocalDate dateOfBirth, Address address, String mailingAddress, String password) {
        super(name, roles);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.mailingAddress = mailingAddress;
        this.password = password;
    }

    @Override
    public User toEntity() {
        Set<Role> r = roles.stream().map(Role::new).collect(Collectors.toSet());
        return new AccountHolder(name, r, dateOfBirth, address, mailingAddress, password);
    }

}
