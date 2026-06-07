package org.example.store.services;

import lombok.RequiredArgsConstructor;
import org.example.store.dto.UserResponse;
import org.example.store.entities.User;
import org.example.store.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //For user  response API
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id , User user){
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found for id "+id ));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(existingUser);
    }

    public UserResponse  getUserById (Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found for id " +id));
        return mapToUserResponse(user);
    }

    public void deleteUser(Long id ){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found with  this id " +id));

        userRepository.delete(user);

    }
}
