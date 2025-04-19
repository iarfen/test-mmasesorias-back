package com.nttdataTest.dto;

import com.nttdataTest.model.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    public String nombre;

    public String correo;

    public String contrasena;

    public List<PhoneDTO> telefonos;
}
