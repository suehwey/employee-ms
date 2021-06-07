package com.garage.upskills.employeems.service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.query.Expression;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.garage.upskills.domain.Employee;
import com.garage.upskills.domain.Training;
import com.garage.upskills.employeems.exception.BadEmployeeDataException;
import com.garage.upskills.employeems.exception.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private CloudantClient client;

    @Autowired
    private Database db;

    public List<String> getAllDB() {
        return client.getAllDbs();
    }

    public List<Employee> getEmployees() {
        try {
            List<Employee> employeeList = db.getAllDocsRequestBuilder()
                    .includeDocs(true)
                    .build()
                    .getResponse()
                    .getDocsAs(Employee.class);
            if (employeeList.size() == 0) throw new Exception("no data found");
            return employeeList;
        } catch (Exception e) {
            throw new EmployeeNotFoundException("No Employee Data");
        }

    }

    public Employee getEmployeeById(String id) {
        try {
            return db.find(Employee.class, id);
        } catch (NoDocumentException e) {
            throw new EmployeeNotFoundException("Employee ID: " + id + " doesn't exist.");
        }
    }

    public Employee getEmployeeByEmpId(String id) {
        List<Employee> employeeList = queryByField("employeeId", id);

        if (employeeList.size() == 0)
            throw new EmployeeNotFoundException("No Employee of employee ID - " + id);

        return employeeList.get(0);
    }

    public List<Employee> getEmployeeByRole(String role) {
        List<Employee> employeeList = queryByField("role", role);

        if (employeeList.size() == 0)
            throw new EmployeeNotFoundException("No Employee of role - " + role);

        return employeeList;
    }

    public List<Employee> getEmployeeByCity(String city) {
        List<Employee> employeeList = queryByField("city", city);

        if (employeeList.size() == 0)
            throw new EmployeeNotFoundException("No Employee lives in city - " + city);

        return employeeList;
    }

    public Employee saveEmployee(Employee employee) {
        // check if employeeId already exists, if not then save to db; otherwise update employee record
        Employee emp;
        try {
            emp = getEmployeeByEmpId(employee.getEmployeeId());
        } catch (EmployeeNotFoundException e) {
            emp = null;
        }
        if (emp == null) {
            Response response = db.save(validateEmployee(employee));
            emp = db.find(Employee.class, response.getId());
        }
        return emp;
    }

    public List<Employee> saveEmployees(List<Employee> employees) {
//        List<Response> responseList = db.bulk(employees);
//        return responseList.stream()
//                .map(response -> db.find(CloudantEmployee.class, response.getId()))
//                .collect(Collectors.toList())
//                ;

        // call addEmployee to do data checking
        return employees.stream()
                .map(employee -> this.saveEmployee(employee))
                .collect(Collectors.toList());
    }

    public String deleteEmployeeById(String id) {
        Employee employee = this.getEmployeeById(id);  // will throw exception if employee id doesn't exist

        db.remove(employee);
        return employee + " has been deleted.";
    }

    public String updateEmployee(Employee employee) {
        this.getEmployeeById(employee.getId());
        db.update(validateEmployee(employee));
        return "Employee ID: " + employee.getId() + " has been updated:\n" + employee;
    }

    public Employee validateEmployee(Employee employee) throws BadEmployeeDataException {
        String empId = employee.getEmployeeId();
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();

        if (empId == null || firstName == null || lastName == null || firstName.length() == 0 || lastName.length() == 0)
            throw new BadEmployeeDataException("Employee First Name and Last Name can't be null.\n" + employee);

        return employee;
    }

    private List<Employee> queryByField(String field, String value) {
        String query = new QueryBuilder(Expression.eq(field, value)).build();
        return db.query(query, Employee.class).getDocs();
    }


    @Value("${training-ms.url}")
    String trainingURI;

    public List<Training> getTrainings(String employeeId) {
        String uri = trainingURI + "/getTrainingByEmpId/" + employeeId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Training>> response = restTemplate
                .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Training>>(){});
        List<Training> trainingList = response.getBody();
        return trainingList;

    }
}
