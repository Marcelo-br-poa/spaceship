package com.w2m.spaceship.controller;

import com.w2m.spaceship.dto.SpaceshipDTO;
import com.w2m.spaceship.service.RabbitService;
import com.w2m.spaceship.service.SpaceshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SpaceshipControllerTest {

    @Mock
    private SpaceshipService spaceshipService;

    @Mock
    private RabbitService rabbitService;

    @InjectMocks
    private SpaceshipController spaceshipController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSpaceships() {
        Pageable pageable = PageRequest.of(0, 10);
        SpaceshipDTO spaceshipDTO = new SpaceshipDTO(1L, "Falcon", "Model X", "Series 1");
        Page<SpaceshipDTO> page = new PageImpl<>(Collections.singletonList(spaceshipDTO));

        when(spaceshipService.findAll(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<SpaceshipDTO>> response = spaceshipController.getAllSpaceships(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        verify(spaceshipService, times(1)).findAll(pageable);
    }

    @Test
    void testGetSpaceshipById() {
        Long id = 1L;
        SpaceshipDTO spaceshipDTO = new SpaceshipDTO(id, "Falcon", "Model X", "Series 1");

        when(spaceshipService.findById(id)).thenReturn(Optional.of(spaceshipDTO));

        ResponseEntity<SpaceshipDTO> response = spaceshipController.getSpaceshipById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(spaceshipDTO, response.getBody());
        verify(spaceshipService, times(1)).findById(id);
    }

    @Test
    void testGetSpaceshipByName() {
        String name = "Falcon";
        SpaceshipDTO spaceshipDTO = new SpaceshipDTO(1L, name, "Model X", "Series 1");

        when(spaceshipService.findByNameContaining(name)).thenReturn(List.of(spaceshipDTO));

        ResponseEntity<List<SpaceshipDTO>> response = spaceshipController.getSpaceshipByName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(spaceshipDTO, response.getBody().get(0));
        verify(spaceshipService, times(1)).findByNameContaining(name);
    }

    @Test
    void testCreateSpaceship() {
        SpaceshipDTO spaceshipDTO = new SpaceshipDTO(null, "Falcon", "Model X", "Series 1");
        SpaceshipDTO savedSpaceshipDTO = new SpaceshipDTO(1L, "Falcon", "Model X", "Series 1");

        when(spaceshipService.save(spaceshipDTO)).thenReturn(savedSpaceshipDTO);

        ResponseEntity<SpaceshipDTO> response = spaceshipController.createSpaceship(spaceshipDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedSpaceshipDTO, response.getBody());
        verify(spaceshipService, times(1)).save(spaceshipDTO);
    }

    @Test
    void testUpdateSpaceship() {
        Long id = 1L;
        SpaceshipDTO spaceshipDTO = new SpaceshipDTO(id, "Falcon", "Model X", "Series 1");

        when(spaceshipService.update(spaceshipDTO)).thenReturn(spaceshipDTO);

        ResponseEntity<SpaceshipDTO> response = spaceshipController.updateSpaceship(id, spaceshipDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(spaceshipDTO, response.getBody());
        verify(spaceshipService, times(1)).update(spaceshipDTO);
    }

    @Test
    void testDeleteSpaceship() {
        Long id = 1L;

        doNothing().when(spaceshipService).deleteById(id);

        ResponseEntity<Void> response = spaceshipController.deleteSpaceship(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(spaceshipService, times(1)).deleteById(id);
    }

    @Test
    void testSendMessageRabbit() {
        String message = "Test message";

        doNothing().when(rabbitService).sendMessageRabbit(message);

        ResponseEntity<String> response = spaceshipController.sendMessageRabbit(message);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Message sent successfully", response.getBody());
        verify(rabbitService, times(1)).sendMessageRabbit(message);
    }



}