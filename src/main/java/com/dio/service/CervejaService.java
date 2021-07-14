package com.dio.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dio.dto.CervejaDTO;
import com.dio.entity.Cerveja;
import com.dio.exceptions.CervejaJaRegistradaException;
import com.dio.exceptions.CervejaNaoEncontradaException;
import com.dio.exceptions.EstoqueCheioException;
import com.dio.mapper.CervejaMapper;
import com.dio.repository.CervejaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaService {
	
	private final CervejaRepository cervejaRepository;
    private final CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    public CervejaDTO createCerveja(CervejaDTO cervejaDTO) throws CervejaJaRegistradaException {
        verifyIfIsAlreadyRegistered(cervejaDTO.getNome());
        Cerveja cerveja = cervejaMapper.toModel(cervejaDTO);
        Cerveja savedCerveja = cervejaRepository.save(cerveja);
        return cervejaMapper.toDTO(savedCerveja);
    }

    public CervejaDTO findByName(String name) throws CervejaNaoEncontradaException {
        Cerveja foundCerveja = cervejaRepository.findByNome(name)
                .orElseThrow(() -> new CervejaNaoEncontradaException(name));
        return cervejaMapper.toDTO(foundCerveja);
    }

    public List<CervejaDTO> listAll() {
        return cervejaRepository.findAll()
                .stream()
                .map(cervejaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws CervejaNaoEncontradaException {
        verifyIfExists(id);
        cervejaRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws CervejaJaRegistradaException {
        Optional<Cerveja> optSavedCerveja = cervejaRepository.findByNome(name);
        if (optSavedCerveja.isPresent()) {
            throw new CervejaJaRegistradaException(name);
        }
    }

    private Cerveja verifyIfExists(Long id) throws CervejaNaoEncontradaException {
        return cervejaRepository.findById(id)
                .orElseThrow(() -> new CervejaNaoEncontradaException(id));
    }

    public CervejaDTO increment(Long id, int quantityToIncrement) throws CervejaNaoEncontradaException, EstoqueCheioException {
        Cerveja cervejaToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + cervejaToIncrementStock.getQuantidade();
        if (quantityAfterIncrement <= cervejaToIncrementStock.getMax()) {
            cervejaToIncrementStock.setQuantidade(cervejaToIncrementStock.getQuantidade() + quantityToIncrement);
            Cerveja incrementedCervejaStock = cervejaRepository.save(cervejaToIncrementStock);
            return cervejaMapper.toDTO(incrementedCervejaStock);
        }
        throw new EstoqueCheioException(id, quantityToIncrement);
    }
}
