package com.kamis.price.domain.raw.entity;

import com.kamis.price.global.enums.RawStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "kamis_raw_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KamisRawRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productClsCode;
    private String countryCode;
    private String regday;
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

    private LocalDateTime createdAt;

    @Builder
    private KamisRawRequest(
            String productClsCode,
            String countryCode,
            String regday,
            String convertKgYn,
            String categoryCode,
            String certKey,
            String certId,
            String returnType,
            String errorCode,
            String rawJson
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
        this.processingStatus = RawStatus.READY;
        this.createdAt = LocalDateTime.now();
    }

    public static KamisRawRequest create(
            String productClsCode,
            String categoryCode,
            String countryCode,
            String regday,
            String certKey,
            String certId
    ) {
        return KamisRawRequest.builder()
                .productClsCode(productClsCode)
                .categoryCode(categoryCode)
                .countryCode(countryCode)
                .regday(regday)
                .convertKgYn("N")
                .certKey(certKey)
                .certId(certId)
                .returnType("json")
                .build();
    }

    public void markFailed(String message) {
        this.processingStatus = RawStatus.FAILED;
        this.processingRetryCount++;
        this.processingErrorMessage = message;
    }

    public void markProcessed() {
        this.processingStatus = RawStatus.PROCESSED;
    }
}
