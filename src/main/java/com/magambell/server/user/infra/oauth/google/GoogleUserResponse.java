package com.magambell.server.user.infra.oauth.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserResponse(
        @JsonProperty("sub")
        String id,
        String email,
        @JsonProperty("name")
        String name,
        @JsonProperty("phone_number")
        String phoneNumber
) {
}
