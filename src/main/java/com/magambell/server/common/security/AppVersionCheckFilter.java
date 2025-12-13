package com.magambell.server.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magambell.server.appversion.app.port.in.dto.AppUpdateInfo;
import com.magambell.server.appversion.app.port.out.AppVersionPolicyQueryPort;
import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import com.magambell.server.appversion.domain.enums.Platform;
import com.magambell.server.common.Response;
import com.magambell.server.common.enums.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 앱 버전 체크 필터
 * - X-App-Version, X-Platform 헤더를 읽어서 버전 정책 검증
 * - minSupportedVersion 미만: 426 Upgrade Required로 차단
 * - recommendedMinVersion 미만: 정상 응답에 updateInfo 포함
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AppVersionCheckFilter extends OncePerRequestFilter {

    private final AppVersionPolicyQueryPort appVersionPolicyQueryPort;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String appVersion = request.getHeader("X-App-Version");
        String platformHeader = request.getHeader("X-Platform");

        // 헤더가 없으면 패스 (웹 브라우저 등)
        if (!StringUtils.hasText(appVersion) || !StringUtils.hasText(platformHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Platform platform = Platform.valueOf(platformHeader.toUpperCase());
            Optional<AppVersionPolicy> policyOpt = appVersionPolicyQueryPort.findByPlatform(platform);

            if (policyOpt.isEmpty()) {
                log.warn("버전 정책을 찾을 수 없음: platform={}", platform);
                filterChain.doFilter(request, response);
                return;
            }

            AppVersionPolicy policy = policyOpt.get();

            // 정책이 비활성화되어 있으면 체크하지 않음
            if (!policy.getActive()) {
                log.debug("버전 정책이 비활성화됨: platform={}", platform);
                filterChain.doFilter(request, response);
                return;
            }

            // 1. 강제 업데이트 체크
            if (policy.requiresForceUpdate(appVersion)) {
                log.warn("강제 업데이트 필요: platform={}, currentVersion={}, minSupportedVersion={}",
                        platform, appVersion, policy.getMinSupportedVersion());
                
                writeForceUpdateResponse(response, policy, appVersion);
                return;
            }

            // 2. 권장 업데이트 체크 (요청은 계속 처리, 응답 헤더에 정보 추가)
            if (policy.recommendsUpdate(appVersion)) {
                log.info("업데이트 권장: platform={}, currentVersion={}, recommendedVersion={}",
                        platform, appVersion, policy.getRecommendedMinVersion());
                
                // 커스텀 헤더로 업데이트 정보 전달
                AppUpdateInfo updateInfo = AppUpdateInfo.recommended(
                        policy.getLatestVersion(),
                        appVersion,
                        policy.getStoreUrl(),
                        policy.getReleaseNotes()
                );
                response.setHeader("X-Update-Available", "true");
                response.setHeader("X-Update-Info", objectMapper.writeValueAsString(updateInfo));
            }

            filterChain.doFilter(request, response);
            
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 플랫폼 값: {}", platformHeader);
            filterChain.doFilter(request, response);
        }
    }

    private void writeForceUpdateResponse(HttpServletResponse response, 
                                         AppVersionPolicy policy, 
                                         String currentVersion) throws IOException {
        response.setStatus(426); // 426 Upgrade Required
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        AppUpdateInfo updateInfo = AppUpdateInfo.forceUpdate(
                policy.getLatestVersion(),
                currentVersion,
                policy.getStoreUrl(),
                policy.getReleaseNotes()
        );

        Response<AppUpdateInfo> errorResponse = new Response<>(updateInfo);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
