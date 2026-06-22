package com.lizardstore.pagoms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenciaResponse {

    private Long ordenId;
    private String preferenciaId;
    private String initPoint;
    private String estado;
    private String publicKey;
    private boolean simulacion;
}
