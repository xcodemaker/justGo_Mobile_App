package com.dhammika_dev.justgo.model.entities.response;

import com.dhammika_dev.justgo.dto.User;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDetailsResponse {
    User user;
    String message;
    int statusCode;
}
