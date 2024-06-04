package org.bigcompany.dao;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Paths.get;
import static java.math.BigDecimal.ZERO;


/**
 * The EmployeeService class provides methods to manage and report on employees.
 * It uses an EmployeeCSVLoader to load employee data from a CSV file, and a SalaryService to calculate salaries.
 * It provides methods to load all employees, determine if an employee is a manager, get the length of an employee's reporting line,
 * get employees with a long reporting line, and generate an employee report.
 *
 * @author Neha B Acharya
 */
public class EmployeeCSVLoader {

    private static final int ID_INDEX = 0;
    private static final int FIRST_NAME_INDEX = 1;
    private static final int LAST_NAME_INDEX = 2;
    private static final int SALARY_INDEX = 3;
    private static final int MANAGER_ID_INDEX = 4;
    private static final int MIN_FIELDS = 4;
    private static final int MAX_FIELDS = 5;
    private static final int ZERO_SALARY = 0;

    /**
     * This method reads a CSV file and builds a map of employees.
     * Each line in the CSV file represents an Employee.
     * The map's keys are Employee IDs, and the values are CompanyStaff objects.
     *
     * @param csvFilePath The path to the CSV file.
     * @return A map of employees.
     * @throws IOException If there is an error reading the CSV file.
     */
    public Map<String, CompanyStaff> buildEmployeeMapFromCSV(String csvFilePath) throws IOException {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        int ceoCount = 0;
        try (BufferedReader reader = newBufferedReader(get(csvFilePath))) {
            String line;
            String headerLine = reader.readLine(); // Skips the header
            if (headerLine == null) {
                throw new IllegalArgumentException("The CSV file is empty");
            }
            while ((line = reader.readLine()) != null) {
                CompanyStaff employee = validateAndCreateEmployeeFromCSVLine(line);
                if (employeeMap.containsKey(employee.getId())) {
                    throw new IllegalArgumentException("Duplicate employee ID: " + employee.getId());
                }
                if (employee.getManagerId() == null) {
                    ceoCount++;
                    if (ceoCount > 1) {
                        throw new IllegalArgumentException("More than one employee without a manager ID");
                    }
                }
                employeeMap.put(employee.getId(), employee);
            }
        }
        return employeeMap;
    }

    /**
     * This method takes a line from the CSV file and constructs a CompanyStaff object.
     * It extracts the Employee's ID, first name, last name, salary, and manager ID from the line.
     *
     * @param csvLine A line from the CSV file.
     * @return A CompanyStaff object representing the Employee.
     */
    private static CompanyStaff validateAndCreateEmployeeFromCSVLine(String csvLine) {
        String[] employeeFields = csvLine.split(",");
        if (employeeFields.length < MIN_FIELDS || employeeFields.length > MAX_FIELDS) {
            throw new IllegalArgumentException("Incorrect number of fields in line: " + csvLine);
        }
        String id = employeeFields[ID_INDEX];
        String firstName = employeeFields[FIRST_NAME_INDEX];
        String lastName = employeeFields[LAST_NAME_INDEX];
        BigDecimal salary = parseSalary(employeeFields[SALARY_INDEX]);
        String managerId = employeeFields.length > MANAGER_ID_INDEX && !employeeFields[MANAGER_ID_INDEX].isEmpty()
                ? employeeFields[MANAGER_ID_INDEX]
                : null;
        // Additional checks for other fields
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("ID, first name, or last name is empty in line: " + csvLine);
        }
        return new Employee(id, firstName, lastName, salary, managerId);
    }

    /**
     * Parses the salary field from the CSV file.
     * It checks if the salary can be parsed as a BigDecimal and is greater than Zero
     * If not it throws IllegalArgumentException
     *
     * @param salaryField The salary field from the CSV file.
     * @return The parsed salary.
     */
    private static BigDecimal parseSalary(String salaryField) {
        try {
            BigDecimal salary = new BigDecimal(salaryField);
            if (salary.compareTo(ZERO) <= ZERO_SALARY) {
                throw new IllegalArgumentException("Salary must be greater than zero");
            }
            return salary;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid salary: " + salaryField, e);
        }
    }
}