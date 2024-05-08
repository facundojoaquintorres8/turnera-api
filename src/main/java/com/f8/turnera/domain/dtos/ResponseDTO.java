package com.f8.turnera.domain.dtos;

import lombok.Data;

@Data
public class ResponseDTO {
    
    private String message;
    private Integer status;
    private Object data;

    public ResponseDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

}
