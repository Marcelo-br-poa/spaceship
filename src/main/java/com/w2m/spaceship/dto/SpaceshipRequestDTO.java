package com.w2m.spaceship.dto;

import jakarta.validation.constraints.NotBlank;

public record SpaceshipRequestDTO(
        @NotBlank String name,
       String model,
       String series) {
}
