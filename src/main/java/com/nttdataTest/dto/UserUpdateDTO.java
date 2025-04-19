package com.nttdataTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateDTO {

    @Builder.Default
    public String nombre = "";

    @Builder.Default
    public String correo = "";

    @Builder.Default
    public String contrasena = "";

    @Builder.Default
    public List<PhoneDTO> telefonos = new ArrayList<>();

    @Builder.Default
    public boolean activo = false;

    @Builder.Default
    public String token = "";
}
