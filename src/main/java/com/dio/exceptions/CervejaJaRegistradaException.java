package com.dio.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CervejaJaRegistradaException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public CervejaJaRegistradaException(String cervejaNome) {
        super(String.format("Cerveja com nome %s já registrada no sistema.", cervejaNome));
    }
}
