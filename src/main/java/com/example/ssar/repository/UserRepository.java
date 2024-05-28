package com.example.ssar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ssar.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);
}