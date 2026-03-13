package com.smartvision.repository.global;

import com.smartvision.entity.global.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCin(String cin);
    Optional<User> findByEmail(String email);
}
