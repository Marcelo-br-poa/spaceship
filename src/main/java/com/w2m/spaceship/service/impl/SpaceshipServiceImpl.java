package com.w2m.spaceship.service.impl;

import com.w2m.spaceship.domain.Spaceship;
import com.w2m.spaceship.dto.SpaceshipDTO;
import com.w2m.spaceship.mapper.SpaceshipMapper;
import com.w2m.spaceship.repository.SpaceshipRepository;
import com.w2m.spaceship.service.SpaceshipService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceshipServiceImpl implements SpaceshipService {

    private static final String ID_NULL = "ID must not be null.";
    private static final String NOT_FOUND_ID = "Spaceship not found with ID: %d";
    private static final String  NAME_EXISTS = "Another spaceship with the same name already exists.";
    private static final String  LOG_CHECK_CACHE = "Retrieving the data '{}' for the cache";
    private static final String  CACHE_BY_ID = "spaceships";
    private static final String  CACHE_BY_NAME = "spaceshipsByName";


    private final CacheManager cacheManager;

    private final SpaceshipRepository repository;
    private final SpaceshipMapper mapper = Mappers.getMapper(SpaceshipMapper.class);

    @Override
    @Transactional(readOnly = true)
    public Page<SpaceshipDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BY_ID, key = "#id")
    public Optional<SpaceshipDTO> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(ID_NULL);
        }

        boolean exists = repository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException(String.format(NOT_FOUND_ID, id));
        }

        log.info(LOG_CHECK_CACHE, id);

        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BY_NAME, key = "#name")
    public List<SpaceshipDTO> findByNameContaining(String name) {

        var matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        var sapceship = new Spaceship();
        sapceship.setName(name);
        Example<Spaceship> exampleQuery = Example.of(sapceship, matcher);

        log.info(LOG_CHECK_CACHE, name);

        return repository.findAll(exampleQuery)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_BY_ID, CACHE_BY_NAME}, allEntries = true)
    public SpaceshipDTO save(SpaceshipDTO spaceshipDTO) {

        var existingSpaceship = repository.findByNameContainingIgnoreCase(spaceshipDTO.name())
                .stream()
                .filter(sc -> sc.getName().equalsIgnoreCase(spaceshipDTO.name()))
                .findFirst();
        if (existingSpaceship.isPresent()) {
            throw new DuplicateKeyException(NAME_EXISTS);
        }
        var spacecraft = mapper.toEntity(spaceshipDTO);
        return mapper.toDTO(repository.save(spacecraft));
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_BY_ID, key = "#spaceshipDTO.id")
    public SpaceshipDTO update(SpaceshipDTO spaceshipDTO) {

        var existingSpaceshipOptional = repository.findById(spaceshipDTO.id());
        var existingSpaceship = existingSpaceshipOptional.orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_ID, spaceshipDTO.id())));

        var anotherSpaceshipWithSameName = repository.findByNameAndIdNot(spaceshipDTO.name(), spaceshipDTO.id());
        if (anotherSpaceshipWithSameName.isPresent()) {
            throw new DuplicateKeyException(NAME_EXISTS);
        }

        existingSpaceship.setName(spaceshipDTO.name());
        existingSpaceship.setModel(spaceshipDTO.model());
        existingSpaceship.setSeries(spaceshipDTO.series());

        return mapper.toDTO(repository.save(existingSpaceship));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(ID_NULL);
        }


        boolean exists = repository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException(String.format(NOT_FOUND_ID, id));
        }

        repository.deleteById(id);
    }

}
