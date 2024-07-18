package com.mysite.springsecurity.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")  // users라는 테이블명으로 테이블 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 파라미터가 없는 기본 생성자를 생성 및 생성자의 접근 수준은 protected로 설정
@Getter // 게터 생성
@Entity // 엔티티
public class User implements UserDetails {  // UserDetails를 상속받아 인증 객체로 사용

    @Id // 기본키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동으로 생성
    @Column(name = "id", updatable = false) // 열의 이름을 id로 지정하고 해당 열은 업데이트 할 수 없음
    private Long id;    // 유저 id

    @Column(name = "email", nullable = false, unique = true)    // 열의 이름을 email로 지정하고 null값을 사용할 수 없고 유니크 값임
    private String email;

    @Column(name = "password", nullable = false)    // 열의 이름을 password로 지정하고 null값을 사용할 수 없음
    private String password;

    // Builder 패턴이란? 객체를 생성하는 생성자와 이를 표현하는 메소드를 분리하여 사용하는 생성 디자인 패턴
    //                  동일한 구성코드를 사용하여 다양한 타입과 표현 제공
    // Builder 패턴의 장점: 필요한 데이터만 사용 가능, 유연성 확보, 가독성 높임, 변경 가능성 최소화
    // 무분별한 Setter 는 데이터 무결성을 해칠 수 있기 때문에 Builder 패턴을 이용해서 변경 가능성 최소화
    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    @Override // 사용자의 권한 반환, UserDetails 인터페이스의 메서드를 재정의
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
        // List.of : 불변 리스트, SimpleGrantedAuthority : 사용자의 권한
    }

    // UserDetails 인터페이스를 구현하는 클래스는 getUsername 과 getPassword 메서드를 반드시 구현해야함
    // UserDetails 참조 &&
    @Override
    public String getUsername() {
        return email; // 이메일 반환
    }

    @Override
    public String getPassword() {
        return password; // 패스워드 반환
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        return true; // ture -> 잠금되지 않음
    }

    @Override // 패스워드 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        return true; // ture -> 사용 가능
    }
}
