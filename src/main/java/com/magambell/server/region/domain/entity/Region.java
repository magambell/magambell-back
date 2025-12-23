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
        @Index(name = "idx_city", columnList = "city"),
        @Index(name = "idx_city_district", columnList = "city, district"),
        @Index(name = "idx_city_district_town", columnList = "city, district, town")
})
@Entity
public class Region extends BaseTimeEntity {

    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "legal_code", nullable = false, unique = true, length = 10)
    private String legalCode;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "town", length = 50)
    private String town;

    @Column(name = "ri", length = 50)
    private String ri;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Builder(access = AccessLevel.PRIVATE)
    public Region(String legalCode, String city, String district, String town, String ri, Boolean isDeleted) {
        this.legalCode = legalCode;
        this.city = city;
        this.district = district;
        this.town = town;
        this.ri = ri;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    public static Region create(String legalCode, String city, String district, String town, String ri, Boolean isDeleted) {
        return Region.builder()
                .legalCode(legalCode)
                .city(city)
                .district(district)
                .town(town)
                .ri(ri)
                .isDeleted(isDeleted)
                .build();
    }
}
