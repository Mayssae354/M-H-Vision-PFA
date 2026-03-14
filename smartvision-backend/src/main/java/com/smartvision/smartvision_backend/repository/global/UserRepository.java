package com.smartvision.smartvision_backend.repository.global;


import com.smartvision.smartvision_backend.entity.global.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByCin(String cin);

    Optional<User> findByEmail(String email);

    boolean existsByCin(String cin);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.cin = :cin AND u.isActive = true")
    Optional<User> findActiveUserByCin(String cin);
}