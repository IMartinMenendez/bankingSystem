package com.ironhack.banking_system.controllers;

import com.ironhack.banking_system.controllers.requests.users.CreateUserRequest;
import com.ironhack.banking_system.controllers.responses.users.CreateUserResponse;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.security.CustomUserDetails;
import com.ironhack.banking_system.services.interfaces.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

  private final UserService userService;

  public UsersController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<CreateUserResponse> getUsers() throws Exception {
    List<User> users = userService.getUsers();
    List<CreateUserResponse> userResponses = new ArrayList<>();
    for(int i = 0; i < users.size(); i++){
      userResponses.add(CreateUserResponse.fromEntity(users.get(i)));
    }
    return userResponses;
  }

  @GetMapping("/{id}")
  public CreateUserResponse getUser(@PathVariable long id) throws Exception {
    return CreateUserResponse.fromEntity(userService.getUser(id).get()); // TODO: Quitar el .get();
  }

  @PostMapping
  public CreateUserResponse createUser(@RequestBody CreateUserRequest user, @AuthenticationPrincipal CustomUserDetails principal) throws Exception {
    return CreateUserResponse.fromEntity(userService.createUser(user.toEntity(), principal.getUser()));
  }
}
