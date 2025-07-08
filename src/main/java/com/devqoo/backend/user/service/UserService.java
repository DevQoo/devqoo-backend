package com.devqoo.backend.user.service;

import static com.devqoo.backend.common.exception.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.devqoo.backend.common.exception.ErrorCode.INVALID_ORIGIN_PASSWORD;
import static com.devqoo.backend.common.exception.ErrorCode.NICKNAME_ALREADY_EXISTS;
import static com.devqoo.backend.common.exception.ErrorCode.USER_NOT_FOUND;

import com.devqoo.backend.comment.repository.CommentRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.post.dto.response.CursorPageResponse;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.user.dto.form.NicknameUpdateForm;
import com.devqoo.backend.user.dto.form.PasswordUpdateForm;
import com.devqoo.backend.user.dto.form.SignUpForm;
import com.devqoo.backend.user.dto.response.UserResponseDto;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import com.devqoo.backend.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    // 회원가입
    @Transactional
    public void signUp(SignUpForm signUpForm) {

        // 이메일, 닉네임 중복 확인
        validateEmail(signUpForm.email());
        validateNickname(signUpForm.nickName());

        // 회원 가입
        User user = User.builder()
            .email(signUpForm.email())
            .nickname(signUpForm.nickName())
            .password(passwordEncoder.encode(signUpForm.password()))
            .profileUrl(null) // S3 개발 후 수정
            .role(UserRoleType.STUDENT)
            .build();

        userRepository.save(user);
    }

    // userId 체크
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    // 이메일 중복 확인
    private void validateEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(EMAIL_ALREADY_EXISTS);
        }
    }

    // 닉네임 중복 확인
    private void validateNickname(String nickname) {

        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(NICKNAME_ALREADY_EXISTS);
        }
    }

    // 닉네임 수정
    @Transactional
    public UserResponseDto updateUserNickname(Long userId, NicknameUpdateForm nicknameUpdateForm) {

        // 닉네임 중복 체크
        String nickname = nicknameUpdateForm.nickname();
        validateNickname(nickname);

        // userId 체크
        User user = this.findById(userId);
        user.updateNickname(nickname);

        return UserResponseDto.from(user);
    }

    // 비밀번호 변경
    @Transactional
    public void updateUserPassword(Long userId, PasswordUpdateForm passwordUpdateForm) {

        // userId 체크
        User user = this.findById(userId);

        // origin 비밀번호 확인
        if (!passwordEncoder.matches(passwordUpdateForm.originPassword(), user.getPassword())) {
            throw new BusinessException(INVALID_ORIGIN_PASSWORD);
        }

        // 비밀번호 변경
        user.updatePassword(passwordEncoder.encode(passwordUpdateForm.password()));
    }

    // 내 게시글 목록
    @Transactional(readOnly = true)
    public CursorPageResponse<PostResponseDto> getMyPosts(Long userId, Long lastPostId, int size) {

        // 게시글 조회
        List<Post> postList = postRepository.searchMyPostsByCursor(userId, lastPostId, size + 1);

        // 다음 게시글 확인
        boolean hasNext = postList.size() > size;
        if (hasNext) {
            postList.remove(size);
        }

        // 댓글 수 조회
        List<Long> postIds = postList.stream().map(Post::getPostId).toList();
        List<Object[]> commentCounts = commentRepository.countCommentsByPostIds(postIds);
        Map<Long, Long> commentCountMap = commentCounts.stream()
            .collect(Collectors.toMap(
                row -> (Long) row[0],
                row -> (Long) row[1]
            ));

        List<PostResponseDto> content = postList.stream()
            .map(post ->
                PostResponseDto.from(post, commentCountMap.getOrDefault(post.getPostId(), 0L)))
            .toList();

        // 다음 게시글
        Long nextPostId = postList.isEmpty() ? null : postList.get(postList.size() - 1).getPostId();

        return CursorPageResponse.of(content, nextPostId, hasNext);
    }

}
