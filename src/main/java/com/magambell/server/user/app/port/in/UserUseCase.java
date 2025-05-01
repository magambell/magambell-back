package com.magambell.server.user.app.port.in;

import com.magambell.server.user.app.port.in.request.RegisterServiceRequest;

public interface UserUseCase {

    void register(RegisterServiceRequest request);
}
