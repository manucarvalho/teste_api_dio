package com.dio.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.dio.dto.CervejaDTO;
import com.dio.exceptions.CervejaJaRegistradaException;
import com.dio.exceptions.CervejaNaoEncontradaException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Gerencia estoque de cerveja")
public interface CervejaControllerDocs {
	
	@ApiOperation(value = "Operação de criação de cervaja")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cerveja criada com sucesso"),
            @ApiResponse(code = 400, message = "Falta preencher algum campo ou campo preenchido com valor fora do intervalo.")
    })
    CervejaDTO createCerveja(CervejaDTO cervejaDTO) throws CervejaJaRegistradaException;

    @ApiOperation(value = "Retorna cerveja encontrada pelo nome")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cerveja encontrada no sistema com sucesso"),
            @ApiResponse(code = 404, message = "Cerveja não encontrada.")
    })
    CervejaDTO findByName(@PathVariable String name) throws CervejaNaoEncontradaException;

    @ApiOperation(value = "Retorna uma lista com todas as cervejas registradas no sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista com todas as cervejas registradas no sistema"),
    })
    List<CervejaDTO> listCervejas();

    @ApiOperation(value = "Deleta a cerveja encontrada por id valido")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Cerveja deletada do sistema com sucesso"),
            @ApiResponse(code = 404, message = "Cerveja não encontrada.")
    })
    void deleteById(@PathVariable Long id) throws CervejaNaoEncontradaException;
}
