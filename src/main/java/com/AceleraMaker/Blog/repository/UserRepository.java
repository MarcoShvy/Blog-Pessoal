package com.AceleraMaker.Blog.repository;

import com.AceleraMaker.Blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsuario(String usuario);

    Optional<User> findByUsuario(String usuario);
}

