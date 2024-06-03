package com.w2m.spaceship.controller.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SpaceshipAspect {

    private static final Logger logger = LoggerFactory.getLogger(SpaceshipAspect.class);

    private static final String ID_NEGATIVE = "ID cannot be negative";

    @Before("execution(* com.w2m.spaceship.controller.SpaceshipController.getSpaceshipById(..)) && args(id)")
    public void aspectNegativeId(Long id) {
        if (id <= 0) {
            logger.warn("Attempt to fetch spaceship with negative ID: {}", id);
            throw new IllegalArgumentException(ID_NEGATIVE);
        }
    }

    @Before("execution(* com.w2m.spaceship.controller.SpaceshipController.updateSpaceship(..)) && args(id, ..)")
    public void aspectNegativeIdUpdate(Long id) {
        if (id <= 0) {
            logger.warn("Attempt to update spaceship with negative ID: {}", id);
            throw new IllegalArgumentException(ID_NEGATIVE);
        }
    }

    @Before("execution(* com.w2m.spaceship.controller.SpaceshipController.deleteSpaceship(..)) && args(id, ..)")
    public void aspectNegativeIdDelete(Long id) {
        if (id <= 0) {
            logger.warn("Attempt to delete spaceship with negative ID: {}", id);
            throw new IllegalArgumentException(ID_NEGATIVE);
        }
    }
}
