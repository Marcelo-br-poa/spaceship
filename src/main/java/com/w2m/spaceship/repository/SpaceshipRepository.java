package com.w2m.spaceship.repository;

import com.w2m.spaceship.domain.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship,Long> {

    @Query("""
           SELECT s 
           FROM Spaceship s 
           WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
           """)
    List<Spaceship> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("""
           SELECT s 
           FROM Spaceship s 
           WHERE LOWER(s.name) = LOWER(:name) AND s.id <> :id
           """)
    Optional<Spaceship> findByNameAndIdNot(@Param("name") String name, @Param("id") Long id);
}
