package org.example.rekollectapi.service;

import org.example.rekollectapi.model.entity.UserEntity;

import java.util.UUID;

public interface UserService {

    UserEntity createGuestUser();

    UserEntity getUser(UUID authenticatedUserId);
}
