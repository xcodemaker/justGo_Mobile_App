package com.dhammika_dev.justgo.model.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDetailsRequest {
    String first_name;
    String last_name;
    String nic_or_passport;
    String address;
    String contact_number;
}
