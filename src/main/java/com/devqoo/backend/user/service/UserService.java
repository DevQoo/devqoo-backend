package com.devqoo.backend.user.service;

import static com.devqoo.backend.common.exception.ErrorCode.USER_NOT_FOUND;

import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    /*
     * 조회 (userId 기준)
     * 존재 하지 않으면 BusinessException 발생
     * */
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }
}
