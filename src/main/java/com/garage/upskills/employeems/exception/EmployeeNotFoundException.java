package com.garage.upskills.employeems.exception;

public class EmployeeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;

    public EmployeeNotFoundException(int id) {
        message = "Employee ID " + id + " doesn't exist.";
    }

    public EmployeeNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
