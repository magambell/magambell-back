package com.magambell.server.common.security;

import com.magambell.server.auth.app.service.JwtService;
import com.magambell.server.user.domain.enums.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        log.info("exception filter");
        String token = request.getHeader("Authorization");

        if (request.getRequestURI().equals("/api/v1/store/approve")) {
            log.info("start");
        }

        if (StringUtils.hasText(token) && jwtService.isValidJwtToken(token)) {
            if (request.getRequestURI().equals("/api/v1/store/approve")) {
                log.info("skip auth for /store/approve");
            } else {
                Long userId = jwtService.getJwtUserId(token);
                UserRole role = jwtService.getJwtUserRole(token);
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        new CustomUserDetails(userId, role),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

                log.info("jwt success");
            }

        }

        filterChain.doFilter(request, response);
    }

}
