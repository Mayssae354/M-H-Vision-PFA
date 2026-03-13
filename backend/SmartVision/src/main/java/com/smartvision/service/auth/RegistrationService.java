package com.smartvision.service.auth;

import com.smartvision.repository.global.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    
    public void registerNewUser() {
        // Logic to validate invite and register user
    }
}
