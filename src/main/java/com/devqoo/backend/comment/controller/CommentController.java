package com.devqoo.backend.comment.controller;

import com.devqoo.backend.comment.service.CommentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentFacade commentFacade;

    // 댓글 목록 조회

    // 댓글 생성

    // 댓글 수정

    // 댓글 삭제

}
