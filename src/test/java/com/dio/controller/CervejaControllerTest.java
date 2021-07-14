package com.dio.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.dio.builder.CervejaDTOBuilder;
import com.dio.dto.CervejaDTO;
import com.dio.dto.QuantidadeDTO;
import com.dio.exceptions.CervejaNaoEncontradaException;
import com.dio.service.CervejaService;
import static com.dio.utils.JsonConvertionUtils.asJsonString;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {
	
	private static final String BEER_API_URL_PATH = "/api/v1/cervejas";
	private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2l;
    private static final String BEER_API_SUBPATH_INCREMENT_URL = "/increment";
    //private static final String BEER_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private CervejaService cervejaService;

    @InjectMocks
    private CervejaController cervejaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cervejaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
        // given
    	CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();

        // when
        when(cervejaService.createCerveja(cervejaDTO)).thenReturn(cervejaDTO);

        // then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
        cervejaDTO.setMarca(null);

        // then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
    	CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();

        //when
        when(cervejaService.findByName(cervejaDTO.getNome())).thenReturn(cervejaDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }
    
    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
    	CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();

        //when
        when(cervejaService.findByName(cervejaDTO.getNome())).thenThrow(CervejaNaoEncontradaException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
    	CervejaDTO beerDTO = CervejaDTOBuilder.builder().build().toBeerDTO();

        //when
        when(cervejaService.listAll()).thenReturn(Collections.singletonList(beerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is(beerDTO.getNome())))
                .andExpect(jsonPath("$[0].marca", is(beerDTO.getMarca())))
                .andExpect(jsonPath("$[0].tipo", is(beerDTO.getTipo().toString())));
    }
    
    @Test
    void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CervejaDTO beerDTO = CervejaDTOBuilder.builder().build().toBeerDTO();

        //when
        when(cervejaService.listAll()).thenReturn(Collections.singletonList(beerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();

        //when
        doNothing().when(cervejaService).deleteById(cervejaDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + cervejaDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(CervejaNaoEncontradaException.class).when(cervejaService).deleteById(INVALID_BEER_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantidadeDTO quantityDTO = QuantidadeDTO.builder()
                .quantidade(10)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toBeerDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantityDTO.getQuantidade());

        when(cervejaService.increment(VALID_BEER_ID, quantityDTO.getQuantidade())).thenReturn(cervejaDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.brand", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.type", is(cervejaDTO.getTipo().toString())))
                .andExpect(jsonPath("$.quantity", is(cervejaDTO.getQuantidade())));
    }
}
