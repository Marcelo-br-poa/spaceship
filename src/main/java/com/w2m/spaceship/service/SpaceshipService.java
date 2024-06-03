package com.w2m.spaceship.service;

import com.w2m.spaceship.dto.SpaceshipDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
public interface SpaceshipService {
    Page<SpaceshipDTO> findAll(Pageable pageable);
    Optional<SpaceshipDTO> findById(Long id);
    List<SpaceshipDTO> findByNameContaining(String name);
    SpaceshipDTO save(SpaceshipDTO spacecraftDTO);
    void deleteById(Long id);
    SpaceshipDTO update(SpaceshipDTO spacecraftDTO);
}
