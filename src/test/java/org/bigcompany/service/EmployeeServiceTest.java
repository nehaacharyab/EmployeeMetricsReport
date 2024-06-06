package org.bigcompany.service;

import org.bigcompany.dao.impl.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;
import org.bigcompany.model.Manager;
import org.bigcompany.service.impl.EmployeeService;
import org.bigcompany.service.impl.ReportingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the functionality of the EmployeeService class.
 *
 * @author Neha B Acharya
 */
class EmployeeServiceTest {

    private EmployeeService employeeService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Employee employee;
    private Manager manager;
    private EmployeeCSVLoader employeeCSVLoader;
    private ReportingService reportingService;

    /**
     * This method is executed before each test. It initializes the mocks and the EmployeeService instance.
     */
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        employee = new Employee("1", "John", "Doe", new BigDecimal("5000"), "2");
        manager = new Manager("2", "Jane", "Doe", new BigDecimal("7000"), null, List.of(employee));
    }

    /**
     * This method is executed after each test. It restores the original System.out stream.
     */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * This test verifies that the loadAllEmployee method returns the correct employees
     * when one employee is a manager and the other a normal employee.
     */
    @Test
    void testLoadAllEmployeeShouldReturnCorrectEmployees() {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        employeeMap.put("1", employee);
        employeeMap.put("2", manager);

        employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        employeeService = new EmployeeService(employeeCSVLoader);

        List<CompanyStaff> result = new ArrayList<>(employeeService.loadAllEmployee().values());

        assertEquals(2, result.size());
        assertEquals(employee.getId(), result.getFirst().getId());
        assertEquals(employee.getFirstName(), result.getFirst().getFirstName());
        assertEquals(manager.getId(), result.getLast().getId());
        assertEquals(manager.getFirstName(), result.getLast().getFirstName());
    }

    /**
     * This test verifies that the loadAllEmployee method returns the correct employees
     * when both the manager and the subordinate are managers themselves.
     */
    @Test
    void testLoadAllEmployeeShouldReturnCorrectEmployeesCase2() {
        // Given
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager("1", "John", "Doe", new BigDecimal("5000"), null, new ArrayList<>()),
                "2", new Manager("2", "Jane", "Doe", new BigDecimal("6000"), "1", new ArrayList<>()));
        employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        employeeService = new EmployeeService(employeeCSVLoader);
        List<CompanyStaff> employees = new ArrayList<>(employeeService.loadAllEmployee().values());

        assertEquals(2, employees.size());
    }

    /**
     * This test verifies that the loadAllEmployee method returns the correct employees
     * when the manager has multiple subordinates.
     */
    static class EmployeeCSVLoaderStub extends EmployeeCSVLoader {
        private final Map<String, CompanyStaff> employeeMap;

        EmployeeCSVLoaderStub(Map<String, CompanyStaff> employeeMap) {
            this.employeeMap = employeeMap;
        }

        @Override
        public Map<String, CompanyStaff> buildEmployeeMapFromCSV(Path csvFilePath) {
            return employeeMap;
        }
    }

}