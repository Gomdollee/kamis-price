package com.kamis.price.domain.raw.entity;

import com.kamis.price.global.entity.BaseEntity;
import com.kamis.price.global.enums.RawStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "kamis_raw_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KamisRawRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== API 요청 파라미터 =====
    private String productClsCode;
    private String countryCode;
    private LocalDate regday;
    private String convertKgYn;
    private String categoryCode;

    // ===== 인증 정보 =====
    private String certKey;
    private String certId;
    private String returnType;

    // ===== 응답 결과 =====
    private String errorCode; // API 에러 코드

    @Lob
    private String rawJson; // 원본 JSON (디버깅 / 재처리용)

    @Enumerated(EnumType.STRING)
    private RawStatus processingStatus; // READY / PROCESSED / FAILED

    private int processingRetryCount; // 재시도 횟수
    private String processingErrorMessage; // 에러 메시지

    @Builder
    private KamisRawRequest(
            String productClsCode,
            String countryCode,
            LocalDate regday,
            String convertKgYn,
            String categoryCode,
            String certKey,
            String certId,
            String returnType,
            String errorCode,
            String rawJson,
            RawStatus processingStatus,
            int processingRetryCount,
            String processingErrorMessage,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.productClsCode = productClsCode;
        this.countryCode = countryCode;
        this.regday = regday;
        this.convertKgYn = convertKgYn;
        this.categoryCode = categoryCode;
        this.certKey = certKey;
        this.certId = certId;
        this.returnType = returnType;
        this.errorCode = errorCode;
        this.rawJson = rawJson;
        this.processingStatus = processingStatus;
        this.processingRetryCount = processingRetryCount;
        this.processingErrorMessage = processingErrorMessage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ===== 생성 팩토리 메서드 =====
    public static KamisRawRequest create(
            String productClsCode,
            String categoryCode,
            String countryCode,
            LocalDate regday,
            String certKey,
            String certId
    ) {
        LocalDateTime now = LocalDateTime.now();

        return KamisRawRequest.builder()
                .productClsCode(productClsCode)
                .categoryCode(categoryCode)
                .countryCode(countryCode)
                .regday(regday)
                .convertKgYn("N")
                .certKey(certKey)
                .certId(certId)
                .returnType("json")
                .processingStatus(RawStatus.READY)
                .processingRetryCount(0)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    // ===== 성공 처리 =====
    public void complete(String errorCode, String rawJson) {
        this.errorCode = errorCode;
        this.rawJson = rawJson;
        this.processingStatus = RawStatus.PROCESSED;
        this.updatedAt = LocalDateTime.now();
    }

    // ===== 실패 처리 =====
    public void markFailed(String message) {
        this.processingStatus = RawStatus.FAILED;
        this.processingRetryCount++;
        this.processingErrorMessage = message;
        this.updatedAt = LocalDateTime.now();
    }
}
