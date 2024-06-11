package com.w2m.spaceship.mapper;

import com.w2m.spaceship.domain.Spaceship;
import com.w2m.spaceship.dto.SpaceshipDTO;
import com.w2m.spaceship.dto.SpaceshipRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpaceshipMapper {

    SpaceshipMapper INSTANCE = Mappers.getMapper(SpaceshipMapper.class);

    @Mapping(target = "id", source = "spaceship.id")
    SpaceshipDTO toDTO(Spaceship spaceship);

    @Mapping(target = "id", ignore = true)
    Spaceship toEntity(SpaceshipDTO spaceshipDTO);

    Spaceship toRequestEntity(SpaceshipRequestDTO spaceshipRequestDTO);

    SpaceshipRequestDTO toRequestDTO(Spaceship spaceship);

}
