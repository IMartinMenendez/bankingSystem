package com.ironhack.banking_system.controllers.responses.users;

import java.util.Set;
public class CreateThirdPartyResponse extends CreateUserResponse {

    public CreateThirdPartyResponse(long id, String name, Set<String> roles) {
        super(id, name, roles);
    }
}
