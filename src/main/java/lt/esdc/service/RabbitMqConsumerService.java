package lt.esdc.service;

import lt.esdc.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitMqConsumerService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqConsumerService.class);

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void receiveMessage(Map<String, Double> message) {
        log.info("Received message from RabbitMQ queue: {}", message);
        // Here you can add any business logic to process the message
    }
}
