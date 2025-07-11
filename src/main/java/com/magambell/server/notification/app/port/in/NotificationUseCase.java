package com.magambell.server.notification.app.port.in;

import com.magambell.server.notification.app.port.in.request.NotifyStoreOpenRequest;
import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.in.request.SaveStoreOpenFcmTokenServiceRequest;

public interface NotificationUseCase {
    void saveStoreOpenToken(SaveStoreOpenFcmTokenServiceRequest request);

    void notifyStoreOpen(NotifyStoreOpenRequest request);

    void saveToken(SaveFcmTokenServiceRequest request);
}
