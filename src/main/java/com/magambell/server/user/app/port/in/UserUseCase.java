package com.magambell.server.user.app.port.in;

import com.magambell.server.user.app.port.in.request.LoginServiceRequest;
import com.magambell.server.user.app.port.in.request.RegisterServiceRequest;

public interface UserUseCase {

    void register(RegisterServiceRequest request);

    void login(LoginServiceRequest request);
}
