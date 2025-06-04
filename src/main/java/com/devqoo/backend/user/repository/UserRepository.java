package com.devqoo.backend.user.repository;

import com.devqoo.backend.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 기준 검색
    Optional<User> findByEmail(String email);

    // 이메일 여부 확인
    boolean existsByEmail(String email);

    // 닉네임 여부 확인
    boolean existsByNickname(String nickName);
}
