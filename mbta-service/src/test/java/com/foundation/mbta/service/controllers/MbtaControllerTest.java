package com.foundation.mbta.service.controllers;

import com.foundation.mbta.service.repo.RouteRepository;
import com.foundation.mbta.service.repo.StopRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = MbtaController.class)
@ContextConfiguration(classes = {RouteRepository.class, StopRepository.class, MbtaController.class})
public class MbtaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StopRepository stopRepository;

    @MockBean
    private RouteRepository routeRepository;


    @Test
    void shouldFetch_CityStops() throws Exception {

        List<Object> stops = List.of(new Object[]{"Somerville", "2"});
        Mockito.when(stopRepository.cityStops("Somerville")).thenReturn(stops);

        this.mockMvc.perform(get("/mbta/cityNumStops"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Somerville"))
                );
    }

}