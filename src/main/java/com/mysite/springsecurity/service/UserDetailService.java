package com.mysite.springsecurity.service;

import com.mysite.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// UserDetailsService: 스프링 시큐리티에서 사용자의 정보를 가져오는 인터페이스
@RequiredArgsConstructor // UserRepository 생성자 자동으로 생성
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {  // 이메일을 통해 사용자의 정보를 로드
        return userRepository.findByEmail(email) // 이메일을 통해 사용자를 검색
                .orElseThrow(() -> new UsernameNotFoundException(email)); // 사용자가 존재하지 않으면 예외처리발생
    }
}
