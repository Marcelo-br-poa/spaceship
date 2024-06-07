package com.w2m.spaceship.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.w2m.spaceship.domain.Spaceship;
import com.w2m.spaceship.dto.SpaceshipDTO;
import com.w2m.spaceship.mapper.SpaceshipMapper;
import com.w2m.spaceship.repository.SpaceshipRepository;
import com.w2m.spaceship.service.RabbitService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SpaceshipServiceImplTest {

    @Mock
    private RabbitService rabbitService;

    @Mock
    private SpaceshipRepository repository;

    @InjectMocks
    private SpaceshipServiceImpl spaceshipService;

    private SpaceshipMapper mapper = Mappers.getMapper(SpaceshipMapper.class);

    private Spaceship spaceship;
    private SpaceshipDTO spaceshipDTO;

    @BeforeEach
    void setUp() {
        spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("Star Destroyer");
        spaceship.setModel("Imperial-class Star Destroyer");
        spaceship.setSeries("Destroyers");

        spaceshipDTO = new SpaceshipDTO(1L, "Star Destroyer", "Imperial-class Star Destroyer", "Destroyers");
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Spaceship> page = new PageImpl<>(List.of(spaceship));

        when(repository.findAll(pageable)).thenReturn(page);

        Page<SpaceshipDTO> result = spaceshipService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(spaceship));

        Optional<SpaceshipDTO> result = spaceshipService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Star Destroyer", result.get().name());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            spaceshipService.findById(1L);
        });

        String expectedMessage = "Spaceship not found with ID: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFindByNameContaining() {
        when(repository.findAll(any(Example.class))).thenReturn(List.of(spaceship));

        List<SpaceshipDTO> result = spaceshipService.findByNameContaining("Star Destroyer");

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll(any(Example.class));
    }

    @Test
    void testSave() {
        when(repository.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of());
        when(repository.save(any(Spaceship.class))).thenReturn(spaceship);

        SpaceshipDTO result = spaceshipService.save(spaceshipDTO);

        assertEquals("Star Destroyer", result.name());
        verify(repository, times(1)).save(any(Spaceship.class));
        verify(rabbitService, times(1)).sendMessageRabbit(anyString());
    }

    @Test
    void testSaveDuplicateName() {
        when(repository.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of(spaceship));

        Exception exception = assertThrows(DuplicateKeyException.class, () -> {
            spaceshipService.save(spaceshipDTO);
        });

        String expectedMessage = "Another spaceship with the same name already exists.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(spaceship));
        when(repository.save(any(Spaceship.class))).thenReturn(spaceship);
        when(repository.findByNameAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());

        SpaceshipDTO result = spaceshipService.update(spaceshipDTO);

        assertEquals("Star Destroyer", result.name());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any(Spaceship.class));
    }

    @Test
    void testDeleteById() {
        when(repository.existsById(anyLong())).thenReturn(true);

        spaceshipService.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            spaceshipService.deleteById(1L);
        });

        String expectedMessage = "Spaceship not found with ID: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
