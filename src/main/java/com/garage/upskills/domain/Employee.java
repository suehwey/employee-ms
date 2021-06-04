package com.garage.upskills.domain;

import com.cloudant.client.api.model.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Document {

    private String employeeId;
    private String firstName;
    private String lastName;
    private String role;
    private String city;
    private String email;
}
