package com.ironhack.banking_system.controllers.responses.accounts;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.*;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateCheckingResponse.class, name = "checking"),
        @JsonSubTypes.Type(value = CreateCreditCardResponse.class, name = "credit_card"),
        @JsonSubTypes.Type(value = CreateSavingsResponse.class, name = "savings"),
        @JsonSubTypes.Type(value = CreateStudentCheckingResponse.class, name = "student")
})
public abstract class CreateAccountResponse {

    @Getter
    protected long id;
    @Getter
    protected Money balance;
    @Getter
    protected long primaryOwnerId;
    @Getter
    protected Long secondaryOwnerId;
    @Getter
    protected Money penaltyFee;
    @Getter
    protected String status;
    @Getter
    protected LocalDateTime createdAt;
    @Getter
    protected LocalDateTime refreshedAt;

    public CreateAccountResponse() {
    }

    public CreateAccountResponse(long id, Money balance, long primaryOwnerId, Long secondaryOwnerId, Money penaltyFee, String status, LocalDateTime createdAt, LocalDateTime refreshedAt) {
        this.id = id;
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.penaltyFee = penaltyFee;
        this.status = status;
        this.createdAt = createdAt;
        this.refreshedAt = refreshedAt;
    }

    public static CreateAccountResponse fromEntity(Account account) throws Exception {
        if(account instanceof CheckingAccount){
            return new CreateCheckingResponse(account.getId(), account.getBalance(), account.getPrimaryOwner().getId(), account.getSecondaryOwner() != null ? account.getSecondaryOwner().getId() : null, account.getPenaltyFee(), account.getStatus().toString(), account.getCreatedAt(), account.getRefreshedAt(), ((CheckingAccount) account).getMinimumBalance(), ((CheckingAccount) account).getMonthlyMaintenanceFee());
        } else if(account instanceof CreditCardAccount){
            return new CreateCreditCardResponse(account.getId(), account.getBalance(), account.getPrimaryOwner().getId(),account.getSecondaryOwner() != null ? account.getSecondaryOwner().getId() : null, account.getPenaltyFee(), account.getStatus().toString(), account.getCreatedAt(), account.getRefreshedAt(), ((CreditCardAccount) account).getCreditLimit(), ((CreditCardAccount) account).getInterestRate());
        }else if(account instanceof SavingsAccount){
            return new CreateSavingsResponse(account.getId(), account.getBalance(), account.getPrimaryOwner().getId(), account.getSecondaryOwner() != null ? account.getSecondaryOwner().getId() : null, account.getPenaltyFee(), account.getStatus().toString(), account.getCreatedAt(), account.getRefreshedAt(), ((SavingsAccount) account).getMinimumBalance(),((SavingsAccount) account).getInterestRate());
        } else if(account instanceof StudentCheckingAccount){
            return new CreateStudentCheckingResponse(account.getId(), account.getBalance(), account.getPrimaryOwner().getId(), account.getSecondaryOwner() != null ? account.getSecondaryOwner().getId() : null, account.getPenaltyFee(), account.getStatus().toString(), account.getCreatedAt(), account.getRefreshedAt());
        }
        throw new Exception("Unknown Account Type");
    }
}
