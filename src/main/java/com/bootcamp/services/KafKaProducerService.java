package com.bootcamp.services;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafKaProducerService
{
  private final Logger LOG = LoggerFactory.getLogger(KafKaConsumerService.class);


  @Autowired
  private KafkaTemplate<String, Object> KafkaTemplate;


  public void post(Object employeePayload, String key, String topic)
  {
    ListenableFuture<SendResult<String, Object>> future =
        this.KafkaTemplate.send(topic, new Date().toString() ,employeePayload);

    future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
      @Override
      public void onSuccess(SendResult<String, Object> result) {
        LOG.info("Employee info sending: " + employeePayload + " with offset: "
            + result.getProducerRecord().toString());
      }
      @Override
      public void onFailure(Throwable ex) {
        LOG.error("Employee record creation failed : " + employeePayload, ex);
      }
    });
  }
}