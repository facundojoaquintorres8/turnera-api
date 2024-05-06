package com.f8.turnera.models;

import lombok.Data;

@Data
public class CustomerFilterDTO extends DefaultFilterDTO {

    private String businessName;
    private String brandName;
    private String email;
    private String phone1;
    
}
