package com.bootcamp.handler;

import com.bootcamp.dto.EmployeePayload;
import com.bootcamp.services.KafKaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeHandler {
  @Autowired
  KafKaProducerService kafKaProducerService;

  public void process(EmployeePayload employeePayload) {
    kafKaProducerService.post(employeePayload.getPayload(), String.valueOf(System.currentTimeMillis()), employeePayload.getTopic());
 }

}
