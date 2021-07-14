package com.dio.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstoqueCheioException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public EstoqueCheioException(Long id, int aumentoQuantidade) {
        super(String.format("Quantidade para adicionar da cerveja com id %s excede a capacidade maxima do estoque: %s", id, aumentoQuantidade));
    }

}
