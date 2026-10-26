package ar.edu.utn.dds.k3003.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Value("${app.rabbitmq.exchange:donatrack.exchange}")
  private String exchangeName;

  @Value("${app.rabbitmq.queue:donadores.cola}")
  private String queueName;

  @Value("${app.rabbitmq.routing-key:donadores.#}")
  private String routingKey;

  @Bean
  public ConnectionFactory connectionFactory(
      @Value("${CLOUDAMQP_URL:${RABBITMQ_URL:}}") String amqpUrl,
      @Value("${spring.rabbitmq.host:localhost}") String host,
      @Value("${spring.rabbitmq.port:5672}") int port,
      @Value("${spring.rabbitmq.username:guest}") String username,
      @Value("${spring.rabbitmq.password:guest}") String password,
      @Value("${spring.rabbitmq.virtual-host:/}") String virtualHost) {
    if (amqpUrl != null && !amqpUrl.isBlank()) {
      CachingConnectionFactory factory = new CachingConnectionFactory();
      factory.setUri(amqpUrl);
      return factory;
    }
    CachingConnectionFactory factory = new CachingConnectionFactory(host, port);
    factory.setUsername(username);
    factory.setPassword(password);
    factory.setVirtualHost(virtualHost);
    return factory;
  }

  @Bean
  public TopicExchange donatrackExchange() {
    return new TopicExchange(exchangeName, true, false);
  }

  @Bean
  public Queue donadoresQueue() {
    return QueueBuilder.durable(queueName).build();
  }

  @Bean
  public Binding donadoresQueueBinding(Queue donadoresQueue, TopicExchange donatrackExchange) {
    return BindingBuilder.bind(donadoresQueue).to(donatrackExchange).with(routingKey);
  }

  @Bean
  public MessageConverter jacksonMessageConverter() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return new Jackson2JsonMessageConverter(mapper);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jacksonMessageConverter);
    return template;
  }
}
