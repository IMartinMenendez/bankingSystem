package com.ironhack.banking_system.controllers.requests.users;

import com.ironhack.banking_system.data.model.users.Role;
import com.ironhack.banking_system.data.model.users.ThirdPartyUser;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateThirdPartyRequest extends CreateUserRequest{
    @Getter
    private String hashedKey;

    public CreateThirdPartyRequest() {
    }

    public CreateThirdPartyRequest(String name, Set<String> roles, String hashedKey) {
        super(name, roles);
        this.hashedKey = hashedKey;
    }

    @Override
    public User toEntity() {
        Set<Role> r = roles.stream().map(Role::new).collect(Collectors.toSet());
        return new ThirdPartyUser(name, r, hashedKey);
    }
}
