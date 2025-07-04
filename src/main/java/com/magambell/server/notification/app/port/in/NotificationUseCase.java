package com.magambell.server.notification.app.port.in;

import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;

public interface NotificationUseCase {
    void saveToken(SaveFcmTokenServiceRequest request);
}
