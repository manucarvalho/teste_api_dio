package com.dio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dio.dto.CervejaDTO;
import com.dio.entity.Cerveja;

@Mapper
public interface CervejaMapper {
	
	 CervejaMapper INSTANCE = Mappers.getMapper(CervejaMapper.class);

	 Cerveja toModel(CervejaDTO cervejaDTO);

	 CervejaDTO toDTO(Cerveja cerveja);
}
