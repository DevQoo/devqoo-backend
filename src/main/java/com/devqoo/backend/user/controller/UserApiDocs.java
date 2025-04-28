package com.devqoo.backend.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Test", description = "Test 관련 API 입니다.")
public interface UserApiDocs {

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
        @ApiResponse(responseCode = "404", description = "페이지 잘못 접근")
    })
    public ResponseEntity<String> testGet();
}
