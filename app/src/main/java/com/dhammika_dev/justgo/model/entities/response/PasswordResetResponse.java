package com.dhammika_dev.justgo.model.entities.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetResponse {
    public String id;
    public String userType;
    public String email;
    public String createdAt;
    public String updatedAt;
    String message;
}
