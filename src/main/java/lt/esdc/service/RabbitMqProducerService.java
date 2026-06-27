package lt.esdc.service;

import lt.esdc.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducerService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqProducerService.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Object message) {
        log.info("Sending message to queue: {}", message);
        rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_NAME, message);
    }
}
