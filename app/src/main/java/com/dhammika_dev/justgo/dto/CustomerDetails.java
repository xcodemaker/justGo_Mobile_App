package com.dhammika_dev.justgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDetails {
    private long user_id;
    private String first_name;
    private String last_name;
    private String district;
    private String date_of_birth;
    private String contact_number;
    private String profile_pic;
}
