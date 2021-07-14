package com.dio.builder;

import com.dio.dto.CervejaDTO;
import com.dio.enums.TipoCerveja;

import lombok.Builder;

@Builder
public class CervejaDTOBuilder {
	
	@Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Brahma";

    @Builder.Default
    private String brand = "Ambev";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private TipoCerveja tipo = TipoCerveja.LAGER;

    public CervejaDTO toBeerDTO() {
        return new CervejaDTO(id,
                name,
                brand,
                max,
                quantity,
                tipo);
    }

}
