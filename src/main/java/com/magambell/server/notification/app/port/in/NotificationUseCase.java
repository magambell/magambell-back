package com.magambell.server.notification.app.port.in;

import com.magambell.server.notification.app.port.in.request.NotifyStoreOpenRequest;
import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;

public interface NotificationUseCase {
    void saveToken(SaveFcmTokenServiceRequest request);

    void notifyStoreOpen(NotifyStoreOpenRequest request);
}
