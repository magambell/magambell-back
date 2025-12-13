package com.magambell.server.appversion.domain.entity;

import com.magambell.server.appversion.domain.enums.Platform;
import com.magambell.server.common.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AppVersionPolicy extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "policy_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Platform platform;

    @Column(nullable = false, length = 20)
    private String latestVersion;

    @Column(nullable = false, length = 20)
    private String minSupportedVersion;

    @Column(length = 20)
    private String recommendedMinVersion;

    @Column(nullable = false)
    private Boolean forceUpdate;

    @Column(length = 500)
    private String androidStoreUrl;

    @Column(length = 500)
    private String iosStoreUrl;

    @Column(columnDefinition = "TEXT")
    private String releaseNotes;

    @Column(nullable = false)
    private Boolean active;

    @Builder(access = AccessLevel.PRIVATE)
    private AppVersionPolicy(final Platform platform, final String latestVersion, 
                            final String minSupportedVersion, final String recommendedMinVersion,
                            final Boolean forceUpdate, final String androidStoreUrl, 
                            final String iosStoreUrl, final String releaseNotes, final Boolean active) {
        this.platform = platform;
        this.latestVersion = latestVersion;
        this.minSupportedVersion = minSupportedVersion;
        this.recommendedMinVersion = recommendedMinVersion;
        this.forceUpdate = forceUpdate != null ? forceUpdate : false;
        this.androidStoreUrl = androidStoreUrl;
        this.iosStoreUrl = iosStoreUrl;
        this.releaseNotes = releaseNotes;
        this.active = active != null ? active : true;
    }

    /**
     * 정책 업데이트
     */
    public void update(final String latestVersion, final String minSupportedVersion,
                      final String recommendedMinVersion, final Boolean forceUpdate,
                      final String androidStoreUrl, final String iosStoreUrl,
                      final String releaseNotes) {
        this.latestVersion = latestVersion;
        this.minSupportedVersion = minSupportedVersion;
        this.recommendedMinVersion = recommendedMinVersion;
        if (forceUpdate != null) this.forceUpdate = forceUpdate;
        this.androidStoreUrl = androidStoreUrl;
        this.iosStoreUrl = iosStoreUrl;
        this.releaseNotes = releaseNotes;
    }

    /**
     * 정책 활성화/비활성화
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    /**
     * 정책 생성
     */
    public static AppVersionPolicy create(final Platform platform, final String latestVersion,
                                         final String minSupportedVersion, final String recommendedMinVersion,
                                         final Boolean forceUpdate, final String androidStoreUrl,
                                         final String iosStoreUrl, final String releaseNotes) {
        return AppVersionPolicy.builder()
                .platform(platform)
                .latestVersion(latestVersion)
                .minSupportedVersion(minSupportedVersion)
                .recommendedMinVersion(recommendedMinVersion)
                .forceUpdate(forceUpdate)
                .androidStoreUrl(androidStoreUrl)
                .iosStoreUrl(iosStoreUrl)
                .releaseNotes(releaseNotes)
                .active(true)
                .build();
    }

    /**
     * 버전 비교 (semantic versioning)
     * @return -1: v1 < v2, 0: v1 == v2, 1: v1 > v2
     */
    public static int compareVersions(String v1, String v2) {
        if (v1 == null || v2 == null) return 0;
        
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        int length = Math.max(parts1.length, parts2.length);
        
        for (int i = 0; i < length; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            
            if (num1 < num2) return -1;
            if (num1 > num2) return 1;
        }
        return 0;
    }

    /**
     * 현재 버전이 최소 지원 버전보다 낮은지 확인 (강제 업데이트 필요)
     */
    public boolean requiresForceUpdate(String currentVersion) {
        return compareVersions(currentVersion, minSupportedVersion) < 0;
    }

    /**
     * 현재 버전이 권장 최소 버전보다 낮은지 확인 (업데이트 권장)
     */
    public boolean recommendsUpdate(String currentVersion) {
        if (recommendedMinVersion == null) return false;
        return compareVersions(currentVersion, recommendedMinVersion) < 0;
    }

    /**
     * 플랫폼에 맞는 스토어 URL 반환
     */
    public String getStoreUrl() {
        return platform == Platform.ANDROID ? androidStoreUrl : iosStoreUrl;
    }
}
