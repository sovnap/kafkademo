package com.bootcamp.config;

import com.bootcamp.services.DlqErrorRecordService;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig
{
  @Value(value = "${spring.kafka.producer.bootstrap-servers}")
  private String bootstrapAddress;


  @Value(value = "${employee.topic.group.id}")
  private String employeeGroupId;

  @Autowired
  DlqErrorRecordService dlqErrorRecordService;


  public ConsumerFactory<String, Object> employeeEntityConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, employeeGroupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return new DefaultKafkaConsumerFactory<String, Object>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Object>
  employeeEntityKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(employeeEntityConsumerFactory());
    factory.setErrorHandler(dlqErrorRecordService);
    return factory;
  }

}