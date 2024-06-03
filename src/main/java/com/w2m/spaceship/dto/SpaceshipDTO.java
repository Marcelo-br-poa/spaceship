package com.w2m.spaceship.dto;

import jakarta.validation.constraints.NotBlank;

public record SpaceshipDTO(
        Long id,
        @NotBlank String name,
        String model,
        String series
) {
}
