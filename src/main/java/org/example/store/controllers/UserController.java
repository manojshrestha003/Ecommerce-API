package org.example.store.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store.dto.UserResponse;
import org.example.store.entities.User;
import org.example.store.services.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;



    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")

    public User updateUser(@PathVariable Long id , @Valid @RequestBody User user ){
        return userService.updateUser(id, user);
    }

    @GetMapping("/{id}")

    public UserResponse  getUserById (@PathVariable Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")

    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }


}