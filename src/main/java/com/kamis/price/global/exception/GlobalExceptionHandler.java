package com.kamis.price.global.exception;

import com.kamis.price.global.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> handleCustomException(CustomException e) {

        return ApiResponse.fail(e.getMessage());
    }

    /**
     * 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {

        return ApiResponse.fail("서버 내부 오류가 발생했습니다.");
    }

}
