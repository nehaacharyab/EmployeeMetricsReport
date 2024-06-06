package org.bigcompany.dao.impl;

import org.bigcompany.dao.IEmployeeCSVLoader;
import org.bigcompany.exception.EmployeeDataException;
import org.bigcompany.exception.InvalidSalaryException;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.newBufferedReader;
import static java.math.BigDecimal.ZERO;

/**
 * The EmployeeCSVLoader class provides methods to load employee data from a CSV file.
 * It reads the CSV file and builds a map of employees.
 * Each line in the CSV file represents an Employee.
 * The map's keys are Employee IDs, and the values are CompanyStaff objects.
 * It uses the IEmployeeCSVLoader interface to provide the method for loading employee data from a CSV file.
 * The specific implementation of how the data is loaded and parsed is left to the implementing class.
 * It throws an EmployeeDataException if there is an error reading employee data from the CSV file.
 * It throws an InvalidSalaryException if the salary is not a valid number or if it's less than or equal to zero.
 */
public class EmployeeCSVLoader implements IEmployeeCSVLoader {

    private static final int ID_INDEX = 0;
    private static final int FIRST_NAME_INDEX = 1;
    private static final int LAST_NAME_INDEX = 2;
    private static final int SALARY_INDEX = 3;
    private static final int MANAGER_ID_INDEX = 4;
    private static final int MIN_CSV_FIELDS = 4;
    private static final int MAX_CSV_FIELDS = 5;
    private static final int ZERO_SALARY = ZERO.intValue();

    /**
     * Reads a CSV file and builds a map of employees.
     * Each line in the CSV file represents an Employee.
     * The map's keys are Employee IDs, and the values are CompanyStaff objects.
     *
     * @param csvFilePath The path to the CSV file.
     * @return A map of employees, keyed by their unique identifiers.
     * @throws EmployeeDataException If there is an error reading employee data from the CSV file.
     */
    public Map<String, CompanyStaff> buildEmployeeMapFromCSV(Path csvFilePath) throws EmployeeDataException{
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        int ceoCount = 0;
        try (BufferedReader reader = newBufferedReader(csvFilePath)) {
            String line;
            String headerLine = reader.readLine(); // Skips the header
            if (headerLine == null) {
                throw new EmployeeDataException("The CSV file is empty");
            }
            while ((line = reader.readLine()) != null) {
                CompanyStaff employee = validateAndCreateEmployeeFromCSVLine(line);
                if (employeeMap.containsKey(employee.getId())) {
                    throw new EmployeeDataException("Duplicate employee ID: " + employee.getId());
                }
                if (employee.getManagerId() == null) {
                    ceoCount++;
                    if (ceoCount > 1) {
                        throw new EmployeeDataException("More than one employee without a manager ID");
                    }
                }
                employeeMap.put(employee.getId(), employee);
            }
        } catch (NoSuchFileException e) {
            System.err.println("The file " + csvFilePath + " does not exist.");
        } catch (IOException e) {
            System.err.println("Error reading employee data from the CSV file");
        }
        return employeeMap;
    }

    /**
     * This method takes a line from the CSV file and constructs a CompanyStaff object.
     * @param csvLine A line from the CSV file.
     * @return A CompanyStaff object representing the Employee.
     */
    private static CompanyStaff validateAndCreateEmployeeFromCSVLine(String csvLine) {
        String[] employeeFields = csvLine.split(",");
        if (employeeFields.length < MIN_CSV_FIELDS || employeeFields.length > MAX_CSV_FIELDS) {
            throw new EmployeeDataException("Incorrect number of fields in line: " + csvLine);
        }
        String id = employeeFields[ID_INDEX];
        String firstName = employeeFields[FIRST_NAME_INDEX];
        String lastName = employeeFields[LAST_NAME_INDEX];
        BigDecimal salary = parseSalary(employeeFields[SALARY_INDEX]);
        String managerId = employeeFields.length > MANAGER_ID_INDEX && !employeeFields[MANAGER_ID_INDEX].isEmpty()
                ? employeeFields[MANAGER_ID_INDEX]
                : null;
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            throw new EmployeeDataException("ID, first name, or last name is empty in line: " + csvLine);
        }
        return new Employee(id, firstName, lastName, salary, managerId);
    }

    /**
     * Parses the salary field from the CSV file.
     * @param salaryField The salary field from the CSV file.
     * @return BigDecimal representing the salary.
     */
    private static BigDecimal parseSalary(String salaryField) {
        try {
            BigDecimal salary = new BigDecimal(salaryField);
            if (salary.compareTo(ZERO) <= ZERO_SALARY) {
                throw new InvalidSalaryException("Salary must be greater than zero");
            }
            return salary;
        } catch (NumberFormatException e) {
            throw new InvalidSalaryException("Invalid salary: " + salaryField, e);
        }
    }
}