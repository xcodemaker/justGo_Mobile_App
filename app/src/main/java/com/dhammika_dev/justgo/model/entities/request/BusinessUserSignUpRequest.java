package com.dhammika_dev.justgo.model.entities.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessUserSignUpRequest {
    private String email;
    private int industry_id;
    private String business_name;
    private String user_type;
    private String district;
    private String description;
    private String website;
    private String contact_number;
    private String password;
    private String logo;
}
