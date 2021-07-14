package com.dio.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CervejaNaoEncontradaException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public CervejaNaoEncontradaException(String cervejaNome) {
        super(String.format("Cerveja com nome %s não encontrada no sistema.", cervejaNome));
    }

    public CervejaNaoEncontradaException(Long id) {
        super(String.format("Cerveja com id %s não encontrada no sistema.", id));
    }

}
