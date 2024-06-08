package com.w2m.spaceship.controller;

import com.w2m.spaceship.service.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/spaceships/messages")
@RequiredArgsConstructor
public class RabbitController {

    private final RabbitService rabbitService;

    @PostMapping()
    public ResponseEntity<String> sendMessageRabbit(@RequestBody String message) {
        rabbitService.sendMessageRabbit(message);
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }
}
