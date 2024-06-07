package com.w2m.spaceship.service.impl.rabbit;

import com.w2m.spaceship.config.rabbit.ConfigRabbit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitConsumer {
    @RabbitListener(queues = ConfigRabbit.QUEUE_NAME)
    public void message(String message) {
        log.info("Message: {}", message);
    }
}
