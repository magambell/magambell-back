package com.magambell.server.user.app.port.in;

import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.user.adapter.out.persistence.UserInfoResponse;
import com.magambell.server.user.app.port.in.request.LoginServiceRequest;
import com.magambell.server.user.app.port.in.request.RegisterServiceRequest;
import com.magambell.server.user.app.port.in.request.UserEditServiceRequest;
import com.magambell.server.user.app.port.out.dto.MyPageStatsDTO;

public interface UserUseCase {

    void register(RegisterServiceRequest request);

    void login(LoginServiceRequest request);

    UserInfoResponse getUserInfo(CustomUserDetails customUserDetails);

    void userEdit(UserEditServiceRequest request);

    MyPageStatsDTO getMyPage(Long userId);
}
