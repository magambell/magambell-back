package com.magambell.server.region.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "region", indexes = {
        @Index(name = "idx_sido", columnList = "sido"),
        @Index(name = "idx_sido_sigungu", columnList = "sido, sigungu"),
        @Index(name = "idx_sido_sigungu_eupmyeondong", columnList = "sido, sigungu, eupmyeondong")
})
@Entity
public class Region extends BaseTimeEntity {

    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "legal_code", nullable = false, unique = true, length = 10)
    private String legalCode;

    @Column(name = "sido", nullable = false, length = 50)
    private String sido;

    @Column(name = "sigungu", length = 50)
    private String sigungu;

    @Column(name = "eupmyeondong", length = 50)
    private String eupmyeondong;

    @Column(name = "ri", length = 50)
    private String ri;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Builder(access = AccessLevel.PRIVATE)
    public Region(String legalCode, String sido, String sigungu, String eupmyeondong, String ri, Boolean isDeleted) {
        this.legalCode = legalCode;
        this.sido = sido;
        this.sigungu = sigungu;
        this.eupmyeondong = eupmyeondong;
        this.ri = ri;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    public static Region create(String legalCode, String sido, String sigungu, String eupmyeondong, String ri, Boolean isDeleted) {
        return Region.builder()
                .legalCode(legalCode)
                .sido(sido)
                .sigungu(sigungu)
                .eupmyeondong(eupmyeondong)
                .ri(ri)
                .isDeleted(isDeleted)
                .build();
    }
}
