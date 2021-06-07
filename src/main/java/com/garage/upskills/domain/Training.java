package com.garage.upskills.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {

    private String trainingId;
    private String employeeId;
    private String description;

    private LocalDate completeDate;

}


