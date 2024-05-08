package com.f8.turnera.security.domain.dtos;

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

    public ResponseDTO(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

}
