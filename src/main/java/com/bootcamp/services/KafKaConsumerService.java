package com.bootcamp.services;

import com.bootcamp.dto.Employee;
import com.bootcamp.validator.EmployeeDataValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafKaConsumerService
{
  private final Logger LOG = LoggerFactory.getLogger(KafKaConsumerService.class);

  @Autowired
  KafKaProducerService kafKaProducerService;
  @Autowired
  DlqErrorRecordService dlqErrorRecordService;
  @Autowired
  EmployeeDataValidator employeeDataValidator;

  @KafkaListener(topics = "employee")
  public void consumeFromTopicEmployee(String employee) {
    LOG.info("Employee info received from topic(employee): "+ employee);
    try {
      Employee empObject = new ObjectMapper().readValue(employee, Employee.class);
      employeeDataValidator.validate(empObject);
      kafKaProducerService.post(empObject, empObject.getEmployeeId(), "employeeTarget");
    } catch (ConstraintViolationException | JsonProcessingException e) {
      dlqErrorRecordService.handleValidationError(employee);
    }

  }

  @KafkaListener(topics = "employeeTarget")
  public void consumeFromTopicNextTaskTopic(Employee employee) {
    LOG.info("Employee info received from topic(employeeTarget): "+ employee);
  }
}