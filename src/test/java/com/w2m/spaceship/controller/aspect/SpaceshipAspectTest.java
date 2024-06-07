package com.w2m.spaceship.controller.aspect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SpaceshipAspectTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private SpaceshipAspect aspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAspectNegativeId() {
        Long negativeId = -1L;

        assertThrows(IllegalArgumentException.class, () -> aspect.aspectNegativeId(negativeId));

    }

    @Test
    void testAspectPositiveId() {
        Long positiveId = 1L;

        assertDoesNotThrow(() -> {
            aspect.aspectNegativeId(positiveId);
        });

        verify(logger, times(0)).warn("Attempt to fetch spaceship with negative ID: {}", positiveId);
    }

    @Test
    void testUpdateNegativeId() {
        Long negativeId = -1L;

        assertThrows(IllegalArgumentException.class, () -> aspect.aspectNegativeId(negativeId));

        assertThrows(IllegalArgumentException.class, () -> aspect.aspectNegativeIdUpdate(negativeId));
    }

    @Test
    void testUpdatePositiveId() {
        Long positiveId = 1L;

        assertDoesNotThrow(() -> {
            aspect.aspectNegativeIdUpdate(positiveId);
        });

        verify(logger, times(0)).warn("Attempt to update spaceship with negative ID: {}", positiveId);
    }

    @Test
    void testDeleteNegativeId() {
        Long negativeId = -1L;

        assertThrows(IllegalArgumentException.class, () -> aspect.aspectNegativeId(negativeId));
    }

    @Test
    void testDeletePositiveId() {
        Long positiveId = 1L;

        assertDoesNotThrow(() -> {
            aspect.aspectNegativeIdDelete(positiveId);
        });

        verify(logger, times(0)).warn("Attempt to delete spaceship with negative ID: {}", positiveId);
    }
}
