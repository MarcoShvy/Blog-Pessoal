package com.AceleraMaker.Blog.repository;

import com.AceleraMaker.Blog.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}

