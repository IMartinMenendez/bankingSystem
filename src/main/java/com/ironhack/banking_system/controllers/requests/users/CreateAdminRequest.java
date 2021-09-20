package com.ironhack.banking_system.controllers.requests.users;

import com.ironhack.banking_system.data.model.users.Admin;
import com.ironhack.banking_system.data.model.users.Role;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateAdminRequest extends CreateUserRequest {
    @Getter
    private String password;

    public CreateAdminRequest() {
    }

    public CreateAdminRequest(String name, Set<String> roles, String password) {
        super(name, roles);
        this.password = password;
    }

    @Override
    public User toEntity() {
        Set<Role> r = roles.stream().map(Role::new).collect(Collectors.toSet());

        return new Admin(name, r, password);
    }
}
