package com.w2m.spaceship.controller;

import com.w2m.spaceship.dto.SpaceshipDTO;
import com.w2m.spaceship.dto.SpaceshipRequestDTO;
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
@RequestMapping("/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

    private final SpaceshipService service;

    @GetMapping
    public ResponseEntity<Page<SpaceshipDTO>> getAllSpaceships(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceshipDTO> getSpaceshipById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SpaceshipDTO>> getSpaceshipByName(@RequestParam String name) {
        return ResponseEntity.ok(service.findByNameContaining(name));
    }

    @PostMapping
    public ResponseEntity<SpaceshipRequestDTO> createSpaceship(@RequestBody SpaceshipRequestDTO dto) {
        var createdSpaceshipDTO = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpaceshipDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceshipRequestDTO> updateSpaceship(@PathVariable Long id, @RequestBody SpaceshipRequestDTO dto) {
        var updatedSpaceshipDTO = service.update(id, new SpaceshipRequestDTO(dto.name(), dto.model(), dto.series()));
        return ResponseEntity.ok(updatedSpaceshipDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpaceship(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
