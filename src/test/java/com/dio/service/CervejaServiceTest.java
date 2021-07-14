package com.dio.service;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.Matchers.lessThan;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import org.hamcrest.MatcherAssert;
//import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
//import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import com.dio.builder.CervejaDTOBuilder;
import com.dio.dto.CervejaDTO;
import com.dio.entity.Cerveja;
import com.dio.exceptions.CervejaJaRegistradaException;
import com.dio.exceptions.CervejaNaoEncontradaException;
import com.dio.exceptions.EstoqueCheioException;
import com.dio.mapper.CervejaMapper;
import com.dio.repository.CervejaRepository;

@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {
	
	private static final long INVALID_BEER_ID = 1L;
	
	@Mock
    private CervejaRepository cervejaRepository;

	
    private CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    @InjectMocks
    private CervejaService cervejaService;
    
    @Test
    void whenCervejaInformedThenItShouldBeCreated() throws CervejaJaRegistradaException {
        // given
    	CervejaDTO expectedCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
    	Cerveja expectedSavedCerveja = cervejaMapper.toModel(expectedCervejaDTO);

        // when
        when(cervejaRepository.findByNome(expectedCervejaDTO.getNome())).thenReturn(Optional.empty());
        when(cervejaRepository.save(expectedSavedCerveja)).thenReturn(expectedSavedCerveja);

        //then
        CervejaDTO createdCervejaDTO = cervejaService.createCerveja(expectedCervejaDTO);

        assertThat(createdCervejaDTO.getId(), is(equalTo(expectedCervejaDTO.getId())));
        assertThat(createdCervejaDTO.getNome(), is(equalTo(expectedCervejaDTO.getNome())));
        assertThat(createdCervejaDTO.getQuantidade(), is(equalTo(expectedCervejaDTO.getQuantidade())));
    }

    @Test
    void whenAlreadyRegisteredCervejaInformedThenAnExceptionShouldBeThrown() {
    	// given
    	CervejaDTO expectedCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
    	Cerveja duplicatedCerveja = cervejaMapper.toModel(expectedCervejaDTO);

        // when
        when(cervejaRepository.findByNome(expectedCervejaDTO.getNome())).thenReturn(Optional.of(duplicatedCerveja));

        // then
        assertThrows(CervejaJaRegistradaException.class, () -> cervejaService.createCerveja(expectedCervejaDTO));
    }
    
    @Test
    void whenValidCervejaNameIsGivenThenReturnACerveja() throws CervejaNaoEncontradaException {
        // given
        CervejaDTO expectedFoundCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
        Cerveja expectedFoundCerveja = cervejaMapper.toModel(expectedFoundCervejaDTO);

        // when
        when(cervejaRepository.findByNome(expectedFoundCerveja.getNome())).thenReturn(Optional.of(expectedFoundCerveja));

        // then
        CervejaDTO foundCervejaDTO = cervejaService.findByName(expectedFoundCervejaDTO.getNome());

        assertThat(foundCervejaDTO, is(equalTo(expectedFoundCervejaDTO)));
    }

    @Test
    void whenNotRegisteredCervejaNameIsGivenThenThrowAnException() {
        // given
    	CervejaDTO expectedFoundCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();

        // when
        when(cervejaRepository.findByNome(expectedFoundCervejaDTO.getNome())).thenReturn(Optional.empty());

        // then
        assertThrows(CervejaNaoEncontradaException.class, () -> cervejaService.findByName(expectedFoundCervejaDTO.getNome()));
    }
    
    @Test
    void whenListCervejaIsCalledThenReturnAListOfCervejas() {
        // given
    	CervejaDTO expectedFoundCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
    	Cerveja expectedFoundCerveja = cervejaMapper.toModel(expectedFoundCervejaDTO);

        //when
        when(cervejaRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundCerveja));

        //then
        List<CervejaDTO> foundListCervejasDTO = cervejaService.listAll();

        assertThat(foundListCervejasDTO, is(not(empty())));
        assertThat(foundListCervejasDTO.get(0), is(equalTo(expectedFoundCervejaDTO)));
    }
    
    @SuppressWarnings("unchecked")
	@Test
    void whenListCervejaIsCalledThenReturnAnEmptyListOfCervejas() {
        //when
        when(cervejaRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<CervejaDTO> foundListCervejasDTO = cervejaService.listAll();

        assertThat(foundListCervejasDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenACervejaShouldBeDeleted() throws CervejaNaoEncontradaException{
        // given
    	CervejaDTO expectedDeletedCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
    	Cerveja expectedDeletedCerveja = cervejaMapper.toModel(expectedDeletedCervejaDTO);

        // when
        when(cervejaRepository.findById(expectedDeletedCervejaDTO.getId())).thenReturn(Optional.of(expectedDeletedCerveja));
        doNothing().when(cervejaRepository).deleteById(expectedDeletedCervejaDTO.getId());

        // then
        cervejaService.deleteById(expectedDeletedCervejaDTO.getId());

        verify(cervejaRepository, times(1)).findById(expectedDeletedCervejaDTO.getId());
        verify(cervejaRepository, times(1)).deleteById(expectedDeletedCervejaDTO.getId());
    }
    
    @Test
    void whenIncrementIsCalledThenIncrementCervejaStock() throws CervejaNaoEncontradaException, EstoqueCheioException {
        //given
        CervejaDTO expectedCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
        Cerveja expectedCerveja = cervejaMapper.toModel(expectedCervejaDTO);

        //when
        when(cervejaRepository.findById(expectedCervejaDTO.getId())).thenReturn(Optional.of(expectedCerveja));
        when(cervejaRepository.save(expectedCerveja)).thenReturn(expectedCerveja);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedCervejaDTO.getQuantidade() + quantityToIncrement;

        // then
        CervejaDTO incrementedCervejaDTO = cervejaService.increment(expectedCervejaDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedCervejaDTO.getQuantidade()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedCervejaDTO.getMax()));
    }
    
    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        CervejaDTO expectedCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
        Cerveja expectedCerveja = cervejaMapper.toModel(expectedCervejaDTO);

        when(cervejaRepository.findById(expectedCervejaDTO.getId())).thenReturn(Optional.of(expectedCerveja));

        int quantityToIncrement = 80;
        assertThrows(EstoqueCheioException.class, () -> cervejaService.increment(expectedCervejaDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        CervejaDTO expectedCervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
        Cerveja expectedCerveja = cervejaMapper.toModel(expectedCervejaDTO);

        when(cervejaRepository.findById(expectedCervejaDTO.getId())).thenReturn(Optional.of(expectedCerveja));

        int quantityToIncrement = 45;
        assertThrows(EstoqueCheioException.class, () -> cervejaService.increment(expectedCervejaDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(cervejaRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(CervejaNaoEncontradaException.class, () -> cervejaService.increment(INVALID_BEER_ID, quantityToIncrement));
    }
}
