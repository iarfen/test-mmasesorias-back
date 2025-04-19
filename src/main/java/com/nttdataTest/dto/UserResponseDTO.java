package com.nttdataTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private Date creado;
    private Date modificado;
    private Date ultimoLogin;
    private String token;
    private boolean activo;
}
