package com.garage.upskills.kafka.consumer;

import com.garage.upskills.domain.Employee;
import com.garage.upskills.employeems.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeConsumer {

    @Autowired
    private EmployeeService service;

    private final Logger logger = LoggerFactory.getLogger(EmployeeConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic}", containerFactory = "kafkaListener")
    public void consume (Employee employee)  {
        logger.info("=== consume: " + employee);
        // add to cloudant database
        Employee savedEmployee = service.saveEmployee(employee);

        logger.info("=== saved to cloudant: " + savedEmployee);
    }
}
