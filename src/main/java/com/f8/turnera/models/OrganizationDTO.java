package com.f8.turnera.models;

import lombok.Data;

@Data
public class OrganizationDTO {

    private Long id;
    private String businessName;
    private String brandName;
    private String cuit;
    private String address;
    private String phone1;
    private String phone2;
    private String defaultEmail;

}
