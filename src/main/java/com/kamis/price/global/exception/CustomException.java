package com.kamis.price.global.exception;

import lombok.Getter;

/**
 * 서비스 로직에서 사용하는 커스텀 예외
 */
@Getter
public class CustomException extends RuntimeException {

    private final String message;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }
}
