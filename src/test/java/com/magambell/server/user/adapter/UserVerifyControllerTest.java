package com.magambell.server.user.adapter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magambell.server.auth.app.service.JwtService;
import com.magambell.server.user.adapter.in.web.VerifyEmailAuthCodeRegisterRequest;
import com.magambell.server.user.adapter.in.web.VerifyEmailDuplicateRegisterRequest;
import com.magambell.server.user.adapter.in.web.VerifyEmailSendRegisterRequest;
import com.magambell.server.user.app.port.in.UserVerifyUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserVerifyController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserVerifyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserVerifyUseCase userVerifyUseCase;
    
    @MockBean
    private JwtService jwtService;

    @DisplayName("회원가입시 이메일 중복이 아닐시 성공한다.")
    @Test
    void emailSignupDuplicate() throws Exception {
        // given
        VerifyEmailDuplicateRegisterRequest request = new VerifyEmailDuplicateRegisterRequest("test@test.com");

        // when // then
        mockMvc.perform(
                        post("/api/v1/verify/email/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입시 이메일 인증번호 전송시 성공한다.")
    @Test
    void emailRegisterSend() throws Exception {
        // given
        VerifyEmailSendRegisterRequest request = new VerifyEmailSendRegisterRequest("test@test.com");

        // when // then
        mockMvc.perform(
                        post("/api/v1/verify/email/register/send")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입시 이메일 인증번호가 일치할시 성공한다.")
    @Test
    void emailRegisterAuthCodeCheck() throws Exception {
        // given
        VerifyEmailAuthCodeRegisterRequest request = new VerifyEmailAuthCodeRegisterRequest("test@test.com", "test");

        // when // then
        mockMvc.perform(
                        post("/api/v1/verify/email/register/authCode")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}