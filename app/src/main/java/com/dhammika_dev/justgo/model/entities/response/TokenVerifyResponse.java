package com.dhammika_dev.justgo.model.entities.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenVerifyResponse {
    int id;
    String email;
    String token = "";
    String created_at;
    String updated_at;
    String message;
}
