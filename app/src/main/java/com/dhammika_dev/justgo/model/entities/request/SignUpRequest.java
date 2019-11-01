package com.dhammika_dev.justgo.model.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String first_name;
    private String last_name;
    private String email;
    private String user_type;
    private String address;
    private String nic_or_passport;
    private String contact_number;
    private String password;
    private String profile_pic;

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", user_type='" + user_type + '\'' +
                ", address='" + address + '\'' +
                ", nic_or_passport='" + nic_or_passport + '\'' +
                ", contact_number='" + contact_number + '\'' +
                ", password='" + password + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                '}';
    }
}
