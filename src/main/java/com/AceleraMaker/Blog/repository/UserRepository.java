package com.AceleraMaker.Blog.repository;

import com.AceleraMaker.Blog.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsuario(String usuario);
}

