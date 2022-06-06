package com.example.backend.user;

import com.example.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserDao extends JpaRepository<User, String> {
    User findByUid(String uid);
    User getUserByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);

}