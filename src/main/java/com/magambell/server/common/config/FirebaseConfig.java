package com.magambell.server.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InternalServerException;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class FirebaseConfig {

    private final Environment environment;

    @Value("${firebase.config.path:config/firebase-service-account.json}")
    private String firebaseConfigPath;

    @Value("${firebase.config.json}")
    private String firebaseConfigJson;

    @PostConstruct
    public void init() {
        try {
            log.info("Loading Firebase config");
            InputStream serviceAccount;

            if (isProdProfile() && !firebaseConfigJson.isBlank()) {
                serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8));
            } else {
                serviceAccount = new FileInputStream(firebaseConfigPath);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerException(ErrorCode.FIREBASE_INIT_FAILED);
        }
    }

    private boolean isProdProfile() {
        for (String profile : environment.getActiveProfiles()) {
            if (profile.equalsIgnoreCase("prod")) {
                return true;
            }
        }
        return false;
    }
}
