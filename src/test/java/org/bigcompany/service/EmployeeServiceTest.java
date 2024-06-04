package org.bigcompany.service;

import org.bigcompany.dao.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;
import org.bigcompany.model.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
    private SalaryService salaryService;

    /**
     * This method is executed before each test. It initializes the mocks and the EmployeeService instance.
     */
    @BeforeEach
    void setUp() {
        salaryService = new SalaryService();
        System.setOut(new PrintStream(outContent));
        employee = new Employee("1", "John", "Doe", new BigDecimal("5000"), "2");
        manager = new Manager("2", "Jane", "Doe", new BigDecimal("7000"), null);
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
        manager.addSubordinate(employee);

        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        employeeMap.put("1", employee);
        employeeMap.put("2", manager);

        employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);

        List<CompanyStaff> result = employeeService.loadAllEmployee();

        assertEquals(2, result.size());
        assertEquals(employee.getId(), result.getFirst().getId());
        assertEquals(employee.getFirstName(), result.getFirst().getFirstName());
        assertEquals(manager.getId(), result.getLast().getId());
        assertEquals(manager.getFirstName(), result.getLast().getFirstName());
    }

    /**
     * This test verifies that the getLongReportingLine method returns the correct employees.
     */
    @Test
    void testGetLongReportingLineShouldReturnCorrectEmployees() {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        employeeMap.put("1", new Employee("1", "Karina", "Cloris", new BigDecimal(10000), null));
        employeeMap.put("2", new Employee("2", "Dulcinea", "Greenwald", new BigDecimal(8998), "1"));
        employeeMap.put("3", new Employee("3", "Neha", "Acharya", new BigDecimal(7000), "2"));
        employeeMap.put("4", new Employee("4", "Anica", "Haldas", new BigDecimal(7000), "3"));
        employeeMap.put("5", new Employee("5", "Blondelle", "Greyson", new BigDecimal(7000), "4"));
        employeeMap.put("6", new Employee("6", "Blondelle", "Greyson", new BigDecimal(7000), "5"));
        employeeMap.put("7", new Employee("7", "Blondelle", "Greyson", new BigDecimal(7000), "6"));

        employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);

        List<CompanyStaff> companyStaffList = employeeService.getLongReportingLine();
        assertEquals(1, companyStaffList.size());
        assertEquals("5", companyStaffList.getFirst().getReportingLineLength());

    }

    /**
     * This test verifies that the loadAllEmployee method returns the correct employees
     * when both the manager and the subordinate are managers themselves.
     */
    @Test
    void testLoadAllEmployeeShouldReturnCorrectEmployeesCase2() {
        // Given
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager("1", "John", "Doe", new BigDecimal("5000"), null),
                "2", new Manager("2", "Jane", "Doe", new BigDecimal("6000"), "1"));
        employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);
        List<CompanyStaff> employees = employeeService.loadAllEmployee();

        assertEquals(2, employees.size());
    }

    /**
     * This test verifies that the getReportingLineLength method returns the correct reporting length.
     */
    @Test
    void testGetReportingLineLengthShouldReturnCorrectLength() {
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager("1", "John", "Doe", new BigDecimal("5000"), null),
                "2", new Manager("2", "Jane", "Doe", new BigDecimal("6000"), "1"));
        employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);
        employeeService.loadAllEmployee();
        int length = employeeService.getReportingLineLength(employee);

        assertEquals(1, length);
    }

    /**
     * This test verifies that the getLongReportingLine method returns an empty list when there are no employees
     * with a reporting line length greater than 4.
     */
    @Test
    void testGetLongReportingLineShouldReturnNoEmployees() {
        employeeCSVLoader = new EmployeeCSVLoaderStub(new HashMap<>());
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);
        employeeService.loadAllEmployee();
        List<CompanyStaff> employees = employeeService.getLongReportingLine();
        assertTrue(employees.isEmpty());
    }

    /**
     * This test verifies that the printPaymentReport method prints the correct report to the console.
     */
    @Test
    void testPrintReportShouldPrintCorrectReport() {
        Map<Manager, BigDecimal> managers = Map.of(
                manager, BigDecimal.valueOf(1000));
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);
        employeeService.printPaymentReport("The overpaid managers", managers);

        String expectedOutput = """
                The overpaid managers
                ------------------------------------
                Jane Doe with ID 2 is overpaid by 1000
                """;
        assertEquals(expectedOutput.lines().count(),
                outContent.toString(StandardCharsets.UTF_8).lines().count());
    }

    static class EmployeeCSVLoaderStub extends EmployeeCSVLoader{
        private final Map<String, CompanyStaff> employeeMap;
        EmployeeCSVLoaderStub(Map<String, CompanyStaff> employeeMap){
            this.employeeMap = employeeMap;
        }
        @Override
        public Map<String, CompanyStaff> buildEmployeeMapFromCSV(String csvFilePath) {
            return employeeMap;
        }
    }

}