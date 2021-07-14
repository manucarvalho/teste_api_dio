package com.dio.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuantidadeDTO {
	
	@NotNull
    @Max(100)
    private Integer quantidade;
}
