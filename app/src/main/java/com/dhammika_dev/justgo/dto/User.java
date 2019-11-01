package com.dhammika_dev.justgo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    String id;
    String user_type;
    String email;
    String first_name;
    String last_name;
    String nic_or_passport;
    String address;
    String contact_number;
    String profile_pic;
}
