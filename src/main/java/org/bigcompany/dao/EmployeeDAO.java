package org.bigcompany.dao;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


/**
 * The EmployeeService class provides methods to manage and report on employees.
 * It uses an EmployeeDAO to load employee data from a CSV file, and a SalaryService to calculate salaries.
 * It provides methods to load all employees, determine if an employee is a manager, get the length of an employee's reporting line,
 * get employees with a long reporting line, and generate an employee report.
 *
 * Assumptions:
 * - The CSV file is always available at the given path.
 * - The CSV file is properly formatted with no missing or malformed data.
 * - The CSV file does not contain any duplicate Employee IDs.
 * - The managerId field is empty only for one employee record which indicates that employee is a CEO
 *
 * @author Neha B Acharya
 */
public class EmployeeDAO {

    private static final int ID_INDEX = 0;
    private static final int FIRST_NAME_INDEX = 1;
    private static final int LAST_NAME_INDEX = 2;
    private static final int SALARY_INDEX = 3;
    private static final int MANAGER_ID_INDEX = 4;

    /**
     * This method reads a CSV file and builds a map of employees.
     * Each line in the CSV file represents an Employee.
     * The map's keys are Employee IDs, and the values are CompanyStaff objects.
     * Assumption: The first line of the CSV file is a header and should be skipped.
     * It also checks if the header is null and throws an exception if it is.
     * It checks for multiple employees without a manager and throws an exception if found.
     * It checks for duplicate employee IDs and throws an exception if found.
     *
     * @param csvFilePath The path to the CSV file.
     * @return A map of employees.
     * @throws IOException If there is an error reading the CSV file.
     */
    public Map<String, CompanyStaff> buildEmployeeMapFromCSV(String csvFilePath) throws IOException {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        int ceoCount = 0;
        try (var reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            String line;
            String headerLine = reader.readLine(); // Skips the header
            if (headerLine == null) {
                throw new IllegalArgumentException("The CSV file is empty");
            }
            while ((line = reader.readLine()) != null) {
                CompanyStaff employee = createEmployeeFromCSVLine(line);
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
     * Assumptions:
     * - Each line in the CSV file has exactly five fields.
     * - The employee id, first name, last name should not be empty
     * - The salary field can be parsed as a BigDecimal and should be greater than zero.
     * - The managerId field can be null only for one employee, indicating that the Employee does not have a manager and
     *   is a CEO.
     *
     * @param csvLine A line from the CSV file.
     * @return A CompanyStaff object representing the Employee.
     */
    private static CompanyStaff createEmployeeFromCSVLine(String csvLine) {
        String[] employeeFields = csvLine.split(",");
        if (employeeFields.length < 4 || employeeFields.length > 5) {
            throw new IllegalArgumentException("Incorrect number of fields in line: " + csvLine);
        }
        var id = employeeFields[ID_INDEX];
        var firstName = employeeFields[FIRST_NAME_INDEX];
        var lastName = employeeFields[LAST_NAME_INDEX];
        BigDecimal salary = parseSalary(employeeFields[SALARY_INDEX]);
        var managerId = employeeFields.length > MANAGER_ID_INDEX && !employeeFields[MANAGER_ID_INDEX].isEmpty()
                ? employeeFields[MANAGER_ID_INDEX]
                : null;
        // Additional checks for other fields
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("ID, first name, or last name is empty in line: " + csvLine);
        }
        return new Employee.Builder()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setSalary(salary)
                .setManagerId(managerId).build();
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
            if (salary.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Salary must be greater than zero");
            }
            return salary;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid salary: " + salaryField, e);
        }
    }
}
