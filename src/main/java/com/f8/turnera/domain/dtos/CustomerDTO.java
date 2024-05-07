package com.f8.turnera.domain.dtos;

import lombok.Data;

@Data
public class CustomerDTO {

    private Long id;
    private Boolean active;
    private Long organizationId;
    private String businessName;
    private String brandName;
    private String cuit;
    private String address;
    private String phone1;
    private String phone2;
    private String email;

}
