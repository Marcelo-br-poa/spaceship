package com.w2m.spaceship.mapper;

import com.w2m.spaceship.domain.Spaceship;
import com.w2m.spaceship.dto.SpaceshipDTO;
import com.w2m.spaceship.dto.SpaceshipRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SpaceshipMapper {

    @Mapping(target = "id", source = "spaceship.id")
    SpaceshipDTO toDTO(Spaceship spaceship);

    Spaceship toRequestEntity(SpaceshipRequestDTO spaceshipRequestDTO);

    SpaceshipRequestDTO toRequestDTO(Spaceship spaceship);

}
