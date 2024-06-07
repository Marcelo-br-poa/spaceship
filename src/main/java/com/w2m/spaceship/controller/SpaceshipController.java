package com.w2m.spaceship.controller;

import com.w2m.spaceship.dto.SpaceshipDTO;
import com.w2m.spaceship.service.RabbitService;
import com.w2m.spaceship.service.SpaceshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

    private final SpaceshipService service;
    private final RabbitService rabbitService;

    @GetMapping
    public ResponseEntity<Page<SpaceshipDTO>> getAllSpaceships(Pageable pageable) {
        var spaceshipDTO = service.findAll(pageable);
        return new ResponseEntity<>(spaceshipDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceshipDTO> getSpaceshipById(@PathVariable Long id) {
        var spaceshipDTO = service.findById(id);
        return spaceshipDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SpaceshipDTO>> getSpaceshipByName(@RequestParam String name) {
        var spaceshipDTO = service.findByNameContaining(name);
        return new ResponseEntity<>(spaceshipDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SpaceshipDTO> createSpaceship(@RequestBody SpaceshipDTO dto) {
        var createSpaceshipDTO = service.save(dto);
        return new ResponseEntity<>(createSpaceshipDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceshipDTO> updateSpaceship(@PathVariable Long id, @RequestBody SpaceshipDTO dto) {
        dto = new SpaceshipDTO(id, dto.name(), dto.model(), dto.series());
        var updatedSpacecraft = service.update(dto);
        return new ResponseEntity<>(updatedSpacecraft, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpaceship(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessageRabbit(@RequestBody String message) {
        rabbitService.sendMessageRabbit(message);
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }

}
