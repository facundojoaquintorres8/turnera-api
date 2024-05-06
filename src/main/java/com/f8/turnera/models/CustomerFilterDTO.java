package com.f8.turnera.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerFilterDTO extends DefaultFilterDTO {

    private String businessName;
    private String brandName;
    private String email;
    private String phone1;
    
}
