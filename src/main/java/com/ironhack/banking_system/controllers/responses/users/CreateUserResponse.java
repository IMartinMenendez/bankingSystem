package com.ironhack.banking_system.controllers.responses.users;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ironhack.banking_system.data.model.users.*;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateAdminResponse.class, name = "admin"),
        @JsonSubTypes.Type(value = CreateAccountHolderResponse.class, name = "account_holder"),
        @JsonSubTypes.Type(value = CreateThirdPartyResponse.class, name = "third_party")
})
public abstract class CreateUserResponse {
    @Getter
    protected long id;
    @Getter
    protected String name;
    @Getter
    protected Set<String> roles;

    public CreateUserResponse(long id, String name, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public static CreateUserResponse fromEntity(User user) throws Exception {
        if(user instanceof Admin){
            return new CreateAdminResponse(user.getId(), user.getName(), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        } else if(user instanceof ThirdPartyUser){
            return new CreateThirdPartyResponse(user.getId(), user.getName(), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        } else if(user instanceof AccountHolder accountHolder){
            return new CreateAccountHolderResponse(user.getId(), user.getName(), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),  accountHolder.getDateOfBirth(), accountHolder.getPrimaryAddress(), accountHolder.getMailingAddress());
        }
        throw new Exception("Unknown User Type");
    }

}
