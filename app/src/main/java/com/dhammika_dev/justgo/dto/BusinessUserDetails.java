package com.dhammika_dev.justgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessUserDetails {
    private long user_id;
    private String business_name;
    private String district;
    private String description;
    private String website;
    private String contact_number;
    private String logo;
}
