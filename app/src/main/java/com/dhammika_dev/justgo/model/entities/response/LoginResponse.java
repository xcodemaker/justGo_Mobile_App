package com.dhammika_dev.justgo.model.entities.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    private String access_token;
    private String token_type;
    private String expires_at;
    private String message;
    private String uid;
    private String user_type;
    private String error;

}
