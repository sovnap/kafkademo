package com.bootcamp.services;

import java.util.Date;
import java.util.List;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class DlqErrorRecordService implements ContainerAwareErrorHandler {
  private final Logger LOG = LoggerFactory.getLogger(DlqErrorRecordService.class);
  @Autowired
  KafKaProducerService kafKaProducerService;

    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records,
        Consumer<?, ?> consumer, MessageListenerContainer container) {

      try {
        if(!records.isEmpty()) {
          ConsumerRecord record = records.get(0);
          LOG.info("Error while sending data to topic :" + record.topic() + " data :" + record.value());
          kafKaProducerService.post( record.value().toString(), record.key().toString(),"dlqTopic");
        }
        } catch (Exception e) {
        LOG.error("Exception: {}", e.getMessage());
      }
    }
  public void handleValidationError(String employee) {
    LOG.info("Error while validating data : {} ", employee);
    kafKaProducerService.post(employee, new Date().toString() , "dlqTopic");
  }

}
