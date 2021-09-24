package com.ironhack.banking_system.controllers;

import com.ironhack.banking_system.controllers.requests.accounts.CreateAccountRequest;
import com.ironhack.banking_system.controllers.responses.accounts.CreateAccountResponse;
import com.ironhack.banking_system.data.AccountRepository;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.security.CustomUserDetails;
import com.ironhack.banking_system.services.interfaces.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

  private final AccountService accountService;

  private final AccountRepository accountRepository;

  public AccountsController(AccountService accountService, AccountRepository accountRepository) {
    this.accountService = accountService;
    this.accountRepository = accountRepository;
  }

  @GetMapping
  public List<CreateAccountResponse> getAccounts(@AuthenticationPrincipal CustomUserDetails principal) throws Exception {
    List<Account> accounts = accountService.getAccounts(principal.getUser());
    List<CreateAccountResponse> accountResponse = new ArrayList<>();
    for(int i = 0; i < accounts.size(); i++){
      accountResponse.add(CreateAccountResponse.fromEntity(accounts.get(i)));
    }
    return accountResponse;
  }

  @GetMapping("/{id}")
  public CreateAccountResponse getAccounts(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails principal) throws Exception {
    return CreateAccountResponse.fromEntity(accountService.getAccount(principal.getUser(), id).get()); // TODO: Quitar el .get();
  }

  @PostMapping
  public CreateAccountResponse createAccount(@RequestBody CreateAccountRequest account) throws Exception {
    return CreateAccountResponse.fromEntity(accountService.createAccount(account));
  }
}
