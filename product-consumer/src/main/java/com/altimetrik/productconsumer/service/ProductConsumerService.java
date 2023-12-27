package com.altimetrik.productconsumer.service;

import com.altimetrik.productconsumer.dto.Product;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumerService {
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue("${rabbitmq.queue}"),
                    exchange = @Exchange("${rabbitmq.exchange}"),
                    key = "${rabbitmq.routingkey}"
            )
    )
    public void receivedProduct(@Payload Product product) {
        System.out.println("Received product: " + product.getName() + " - $" + product.getPrice());
    }
}
