package org.example.rekollectapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.model.entity.UserEntity;
import org.example.rekollectapi.repository.UserRepository;
import org.example.rekollectapi.service.UserService;
import org.example.rekollectapi.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final String GUEST_USERNAME = "Guest";


    @Override
    public UserEntity createGuestUser() {
        UserEntity guestUser = new UserEntity();
        guestUser.setUsername(GUEST_USERNAME);
        guestUser.setPassword("guest");
        guestUser.setEmail("guest@example.com");
        return userRepository.save(guestUser);
    }

    @Override
    public UserEntity getUser(UUID authenticatedUserId) {
        return (authenticatedUserId != null)
                ? userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                : userRepository.findByUsername(GUEST_USERNAME)
                .orElseGet(this::createGuestUser);
    }
}
