package com.mysite.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

// JpaRepository<관리할 엔티티 클래스, 엔티티의 ID 타입>
// JpaRepository 인터페이스를 확장하여 기본적인 CURD, 페이징, 정렬 기능 제공
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // 이메일로 사용자 정보를 가져옴
}
