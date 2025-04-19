package com.nttdataTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PhoneDTO {
    private Long numero;
    private Long codigoCiudad;
    private Long codigoPais;
}
