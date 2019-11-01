package com.dhammika_dev.justgo.model.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
//    private Boolean remember_me;
}
