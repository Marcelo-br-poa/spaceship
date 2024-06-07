package com.w2m.spaceship.service.impl.rabbit;

import com.w2m.spaceship.config.rabbit.ConfigRabbit;
import com.w2m.spaceship.service.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitServiceImpl implements RabbitService {

    private final RabbitTemplate rabbitTemplate;
    @Override
    public void sendMessageRabbit(String message) {
        rabbitTemplate.convertAndSend(ConfigRabbit.EXCHANGE_NAME, ConfigRabbit.ROUTING_KEY, message);
    }
}
