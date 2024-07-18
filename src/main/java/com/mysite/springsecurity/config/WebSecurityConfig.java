package com.mysite.springsecurity.config;

import com.mysite.springsecurity.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() { // WebSecurityCustomizer : 보안 필터 체인에서 제외할 요청들을 정의할 수 있음
        return (web) -> web.ignoring()  // 지정된 요청들을 인증이나 권한 부여를 무시하고 접근할 수 있음
                .requestMatchers(toH2Console()) // h2database 콘솔에 대한 요청 무시, toH2Console() 메서드는 보통 H2 콘솔에 접근하는 URL 패턴을 정의
                .requestMatchers("/static/**"); // /static/** 경로로 시작하는 모든 요청 무시
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {    // 스프링 시큐리티 설정에서 보안 필터 체인을 정의하는 메서드
        return http
                .authorizeHttpRequests() // 요청된 인증, 인가 설정
                .requestMatchers("/login", "/signup", "/user").permitAll() // 해당 경로는 모든 사용자가 접근할 수 있도록 허용
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증된 사용자만 접근할 수 있도록 설정
                .and()
                .formLogin() // 폼 기반 로그인 설정
                .loginPage("/login") // 로그인 페이지 경로 설정
                .defaultSuccessUrl("/home") // 로그인 성공 시 리디렉션할 기본 URL 을 설정
                .and()
                .logout() // 로그아웃 설정
                .logoutSuccessUrl("/login") // 로그아웃 성공 시 리디렉션할 URL 설정
                .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                .and()
                .csrf().disable() // 실습을 위해 잠깐 csrf 비활성화
                .build(); // 빌드
    }

    @Bean // AuthenticationManager : 스프링 스큐리티에서 사용자 인증을 처리하는 인터페이스
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userService) throws Exception {
                                                        // HttpSecurity : 스프링 스큐리티의 보안 설정을 위한 객체
                                                        // BCryptPasswordEncoder : 비밀번호 암호화하기 위한 인코더

        return http
                .getSharedObject(AuthenticationManagerBuilder.class) // http 에서 AuthenticationManagerBuilder 를 가져옴
                .userDetailsService(userService) // 사용자 정보를 로드하는 UserDetailService 를 설정
                .passwordEncoder(bCryptPasswordEncoder) // 비밀번호를 암호화하기 위한 인코더 설정
                .and()
                .build(); // 설정 완료 후 빌드
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

