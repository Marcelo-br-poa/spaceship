package com.w2m.spaceship.controller;


import com.w2m.spaceship.service.RabbitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RabbitControllerTest {

    @Mock
    private RabbitService rabbitService;

    @InjectMocks
    private RabbitController rabbitController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessageRabbit() {
        String message = "Test message";

        doNothing().when(rabbitService).sendMessageRabbit(message);

        ResponseEntity<String> response = rabbitController.sendMessageRabbit(message);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Message sent successfully", response.getBody());
        verify(rabbitService, times(1)).sendMessageRabbit(message);
    }

}