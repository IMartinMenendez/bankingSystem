package com.ironhack.banking_system.controllers.requests.accounts;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.*;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateCheckingRequest.class, name = "checking"),
        @JsonSubTypes.Type(value = CreateCreditCardRequest.class, name = "credit_card"),
        @JsonSubTypes.Type(value = CreateSavingsRequest.class, name = "savings"),
        @JsonSubTypes.Type(value = CreateStudentCheckingRequest.class, name = "student")
})
public abstract class CreateAccountRequest {

    @Getter
    protected Money balance;
    @Getter
    protected long primaryOwnerId;
    @Getter
    protected Long secondaryOwnerId;
    @Getter
    protected String status;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(Money balance, long primaryOwnerId, Long secondaryOwnerId, String status) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.status = status;
    }

    public abstract Account toEntity(User primaryOwner, User secondaryOwner) throws Exception;
}
