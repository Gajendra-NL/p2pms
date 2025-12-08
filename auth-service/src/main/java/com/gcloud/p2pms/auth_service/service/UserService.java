package com.gcloud.p2pms.auth_service.service;

import com.gcloud.p2pms.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.gcloud.p2pms.auth_service.model.User;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
