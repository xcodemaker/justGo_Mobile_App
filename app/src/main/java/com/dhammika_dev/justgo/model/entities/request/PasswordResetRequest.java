package com.dhammika_dev.justgo.model.entities.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    String email;
    String password;
    String password_confirmation;
    String token;
}
