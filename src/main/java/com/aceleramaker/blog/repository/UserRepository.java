package com.aceleramaker.blog.repository;

import com.aceleramaker.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsuario(String usuario);

    Optional<User> findByUsuario(String usuario);
}

