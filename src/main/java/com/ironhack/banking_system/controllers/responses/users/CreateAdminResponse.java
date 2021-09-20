package com.ironhack.banking_system.controllers.responses.users;

import java.util.Set;
public class CreateAdminResponse extends CreateUserResponse {

    public CreateAdminResponse(long id, String name, Set<String> roles) {
        super(id, name, roles);
    }
}
