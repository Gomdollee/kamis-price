package com.kamis.price.global.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 공통 API 응답 객체
 */
@Getter
@Builder
public class ApiResponse<T> {

    private boolean success;

    private int count;

    private String message;

    private T data;

    /**
     * 조회 성공 응답
     */
    public static <T> ApiResponse<T> success(T data, int count) {

        return ApiResponse.<T>builder()
                .success(true)
                .count(count)
                .message("조회 성공")
                .data(data)
                .build();
    }

    /**
     * 실패 응답
     * 요건엔 없었지만 임시로 작성
     */
    public static ApiResponse<Void> fail(String message) {

        return ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .count(0)
                .build();
    }



}
