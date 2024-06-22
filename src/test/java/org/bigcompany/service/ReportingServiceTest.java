package org.bigcompany.service;


import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;
import org.bigcompany.model.Manager;
import org.bigcompany.service.impl.EmployeeService;
import org.bigcompany.service.impl.ReportingService;
import org.bigcompany.service.impl.SalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportingServiceTest {

    private ReportingService reportingService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private SalaryService salaryService;
    private EmployeeService employeeService;
    private Employee employee;
    private Manager manager;
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        salaryService = new SalaryService();
        employeeService = new EmployeeServiceStub(new HashMap<>());
        reportingService = new ReportingService(employeeService, salaryService);
        employee = new Employee("1", "John", "Doe", new BigDecimal("5000"), "2");
        manager = new Manager("2", "Jane", "Doe", new BigDecimal("1000"), null, List.of(employee));

    }

    /**
     * This test verifies that the getLongReportingLine method returns the correct employees.
     */
    @Test
    void testGetLongReportingLineShouldReturnCorrectEmployees() {
        Map<String, CompanyStaff> employeeMap = populateEmployeeMap();
        employeeService = new EmployeeServiceStub(employeeMap);
        reportingService = new ReportingService(employeeService, salaryService);
        Map<CompanyStaff, Integer> companyStaffList = reportingService.getEmployeeReportingLineLengths();
        assertEquals(2, companyStaffList.size());
        assertEquals(6, companyStaffList.get(employeeMap.get("7")));
        assertEquals(5, companyStaffList.get(employeeMap.get("6")));
    }

    /**
     * This test verifies that the generateEmployeeReport method prints the correct manager salary
     * underpayment amount and reporting line length report.
     */
    @Test
    void testGenerateEmployeeReport() {
        Map<String, CompanyStaff> employeeMap = populateEmployeeMap();
        employeeMap.put("1", employee);
        employeeMap.put("2", manager);
        employeeService = new EmployeeServiceStub(employeeMap);
        reportingService = new ReportingService(employeeService, salaryService);
        reportingService.generateEmployeeReport();
        String underpaidManager = "Jane Doe with ID 2 is underpaid by 5000.00";
        String employeeWithLongReportingLine = "Blondelle Greyson with ID 7 has a reporting line of length 5";
        assertTrue(outContent.toString(StandardCharsets.UTF_8)
                .lines()
                .anyMatch(line -> line.contains(underpaidManager)));
        assertTrue(outContent.toString(StandardCharsets.UTF_8)
                .lines()
                .anyMatch(line -> line.contains(employeeWithLongReportingLine)));
    }

    /**
     * This test verifies that the getLongReportingLine method returns no employees when there are no employees.
     */
    @Test
    void testGetLongReportingLineShouldReturnNoEmployees() {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        employeeService = new EmployeeServiceStub(employeeMap);
        Map<CompanyStaff, Integer> employees = reportingService.getEmployeeReportingLineLengths();
        assertTrue(employees.isEmpty());
    }

    /**
     * This test verifies that the printPaymentReport method prints the correct manager salary overpayment report.
     */
    @Test
    void testPrintReportShouldPrintCorrectReport() {
        Map<Manager, BigDecimal> managers = Map.of(
                manager, BigDecimal.valueOf(1000));
        reportingService.printPaymentReport("The overpaid managers", managers);

        String expectedOutput = """
                The overpaid managers
                ------------------------------------
                Jane Doe with ID 2 is overpaid by 1000
                """;
        assertEquals(expectedOutput.lines().count(),
                     outContent.toString(StandardCharsets.UTF_8).lines().count());
    }

   /**
    * This stub class is used to test the ReportingService class by loading the employeeMap.
    */
    static class EmployeeServiceStub extends EmployeeService {
        private final Map<String, CompanyStaff> employeeMap;

        public EmployeeServiceStub(Map<String, CompanyStaff> employeeMap) {
            super(null); // This is a stub, so we don't need to pass the actual dependencies
            this.employeeMap = employeeMap;
        }

        @Override
        public Map<String, CompanyStaff> loadAllEmployee() {
            return employeeMap;
        }
    }

    /**
     * This method populates the employee map with test data.
     * @return A map of employees, keyed by their unique identifiers.
     */
    private static Map<String, CompanyStaff> populateEmployeeMap() {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        employeeMap.put("1", new Employee("1", "Karina", "Cloris", new BigDecimal(10000), null));
        employeeMap.put("2", new Employee("2", "Dulcinea", "Greenwald", new BigDecimal(8998), "1"));
        employeeMap.put("3", new Employee("3", "Neha", "Acharya", new BigDecimal(7000), "2"));
        employeeMap.put("4", new Employee("4", "Anica", "Haldas", new BigDecimal(7000), "3"));
        employeeMap.put("5", new Employee("5", "Blondelle", "Greyson", new BigDecimal(7000), "4"));
        employeeMap.put("6", new Employee("6", "Blondelle", "Greyson", new BigDecimal(7000), "5"));
        employeeMap.put("7", new Employee("7", "Blondelle", "Greyson", new BigDecimal(7000), "6"));
        return employeeMap;
    }

}