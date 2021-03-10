package com.bootcamp.controller;

import com.bootcamp.dto.EmployeePayload;
import com.bootcamp.handler.EmployeeHandler;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = EmployeeController.ENDPOINT)
public class EmployeeController {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  public static final String ENDPOINT = "/kafkademo/employee";

  @Autowired
  private EmployeeHandler handler;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void process(@Valid @RequestBody EmployeePayload employeePayload) {
    handler.process(employeePayload);
  }
}
