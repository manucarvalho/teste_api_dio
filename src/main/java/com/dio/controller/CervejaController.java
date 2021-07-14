package com.dio.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dio.dto.CervejaDTO;
import com.dio.dto.QuantidadeDTO;
import com.dio.exceptions.CervejaJaRegistradaException;
import com.dio.exceptions.CervejaNaoEncontradaException;
import com.dio.exceptions.EstoqueCheioException;
import com.dio.service.CervejaService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cervejas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaController implements CervejaControllerDocs{

	private final CervejaService cervejaService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CervejaDTO createCerveja(@RequestBody @Valid CervejaDTO cervejaDTO) throws CervejaJaRegistradaException {
		return cervejaService.createCerveja(cervejaDTO);
	}

	@GetMapping("/{nome}")
	public CervejaDTO findByName(@PathVariable String nome) throws CervejaNaoEncontradaException {
		return cervejaService.findByName(nome);
	}

	@GetMapping
	public List<CervejaDTO> listCervejas() {
		return cervejaService.listAll();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) throws CervejaNaoEncontradaException {
		cervejaService.deleteById(id);
	}

	@PatchMapping("/{id}/increment")
	public CervejaDTO increment(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO)
			throws CervejaNaoEncontradaException, EstoqueCheioException {
		return cervejaService.increment(id, quantidadeDTO.getQuantidade());
	}
	
}
