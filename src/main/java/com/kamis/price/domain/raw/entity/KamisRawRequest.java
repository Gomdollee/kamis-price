package com.kamis.price.domain.raw.entity;

import com.kamis.price.global.BaseEntity;
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

    private String productClsCode;
    private String countryCode;
    private LocalDate regday;
    private String convertKgYn;
    private String categoryCode;

    private String certKey;
    private String certId;
    private String returnType;

    private String errorCode;

    @Lob
    private String rawJson;

    @Enumerated(EnumType.STRING)
    private RawStatus processingStatus;

    private int processingRetryCount;
    private String processingErrorMessage;

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

    public void complete(String errorCode, String rawJson) {
        this.errorCode = errorCode;
        this.rawJson = rawJson;
        this.processingStatus = RawStatus.PROCESSED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(String message) {
        this.processingStatus = RawStatus.FAILED;
        this.processingRetryCount++;
        this.processingErrorMessage = message;
        this.updatedAt = LocalDateTime.now();
    }
}
