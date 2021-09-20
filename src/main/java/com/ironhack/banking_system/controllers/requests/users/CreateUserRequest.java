package com.ironhack.banking_system.controllers.requests.users;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;

import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateAdminRequest.class, name = "admin"),
        @JsonSubTypes.Type(value = CreateAccountHolderRequest.class, name = "account_holder"),
        @JsonSubTypes.Type(value = CreateThirdPartyRequest.class, name = "third_party")
})
public abstract class CreateUserRequest {
    @Getter
    protected String name;
    @Getter
    protected Set<String> roles;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public abstract User toEntity();
}
