package org.bigcompany.service;

import org.bigcompany.dao.EmployeeDAO;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;
import org.bigcompany.model.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    private EmployeeService employeeService;
    @Mock
    private EmployeeDAO employeeDAO;
    @Mock
    private SalaryService salaryService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private static final String EMPTY_CSV = "src/test/resources/empty.csv";
    private static final String HEADER_ONLY_CSV = "src/test/resources/header_only_test.csv";
    private static final String DUPLICATE_IDS_CSV = "src/test/resources/big_company_duplicates.csv";
    private static final String INVALID_SALARY_CSV = "src/test/resources/big_company_malformed_salary_test.csv";
    private static final String INVALID_NAME_CSV = "src/test/resources/big_company_malformed_name_test.csv";
    private static final String MULTIPLE_CEOS_CSV = "src/test/resources/big_company_two_ceo.csv";
    private static final String NEGATIVE_SALARY_CSV = "src/test/resources/big_company_neg_sal.csv";
    private static final String ZERO_SALARY_CSV = "src/test/resources/big_company_zero_sal.csv";
    private static final String WRONG_PATH_TO_FILE = "wrong/path/to/file.csv";

    private Employee employee;
    private Manager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        employeeService = new EmployeeService(employeeDAO, salaryService);
        System.setOut(new PrintStream(outContent));
        employee = new Employee.Builder()
                .setId("1")
                .setFirstName("John")
                .setLastName("Doe")
                .setSalary(new BigDecimal("5000"))
                .setManagerId("2")
                .build();

        manager = new Manager.Builder()
                .setId("2")
                .setFirstName("Jane")
                .setLastName("Doe")
                .setSalary(new BigDecimal("7000"))
                .setManagerId(null)
                .build();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testLoadAllEmployee_EmptyCSV() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(EMPTY_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_WrongPath() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(WRONG_PATH_TO_FILE);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_OnlyHeader_ReturnsEmptyEmployeeMap() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(HEADER_ONLY_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_DuplicateIds_ReturnsEmptyEmployeeMap() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(DUPLICATE_IDS_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_InvalidSalary_ReturnsEmptyEmployeeMap() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(INVALID_SALARY_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_InvalidName_ReturnsEmptyEmployeeMap() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(INVALID_NAME_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_MultipleCeos_ReturnsEmptyEmployeeMap() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(MULTIPLE_CEOS_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_NegativeSalary_ReturnsEmptyEmployeeMap() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(NEGATIVE_SALARY_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testLoadAllEmployee_ZeroSalary_ReturnsEmptyEmployeeMap() throws IOException {
        Mockito.doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(ZERO_SALARY_CSV);
        var employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void loadAllEmployeeReturnsCorrectEmployees() throws IOException {
        manager.addSubordinate(employee);

        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        employeeMap.put("1", employee);
        employeeMap.put("2", manager);

        when(employeeDAO.buildEmployeeMapFromCSV(anyString()))
                .thenReturn(employeeMap);

        List<CompanyStaff> result = employeeService.loadAllEmployee();

        assertEquals(2, result.size());
        assertEquals(employee.getId(), result.getFirst().getId());
        assertEquals(employee.getFirstName(), result.getFirst().getFirstName());
        assertEquals(manager.getId(), result.getLast().getId());
        assertEquals(manager.getFirstName(), result.getLast().getFirstName());
    }

    @Test
    void getLongReportingLine() throws IOException {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        Employee employee1 = new Employee.Builder()
                .setId("1")
                .setFirstName("Karina")
                .setLastName("Cloris")
                .setManagerId(null)
                .setSalary(new BigDecimal(10000))
                .build();
        Employee employee2 = new Employee.Builder()
                .setId("2")
                .setFirstName("Dulcinea")
                .setLastName("Greenwald")
                .setManagerId("1")
                .setSalary(new BigDecimal(8998))
                .build();
        Employee employee3 = new Employee.Builder()
                .setId("3")
                .setFirstName("Neha")
                .setLastName("Acharya")
                .setManagerId("2")
                .setSalary(new BigDecimal(7000))
                .build();
        Employee employee4 = new Employee.Builder()
                .setId("4")
                .setFirstName("Anica")
                .setLastName("Haldas")
                .setManagerId("3")
                .setSalary(new BigDecimal(7000))
                .build();
        Employee employee5 = new Employee.Builder()
                .setId("5")
                .setFirstName("Blondelle")
                .setLastName("Greyson")
                .setManagerId("4")
                .setSalary(new BigDecimal(7000))
                .build();
        Employee employee6 = new Employee.Builder()
                .setId("6")
                .setFirstName("Blondelle")
                .setLastName("Greyson")
                .setManagerId("5")
                .setSalary(new BigDecimal(7000))
                .build();
        Employee employee7 = new Employee.Builder()
                .setId("7")
                .setFirstName("Blondelle")
                .setLastName("Greyson")
                .setManagerId("6")
                .setSalary(new BigDecimal(7000))
                .build();

        employeeMap.put("1", employee1);
        employeeMap.put("2", employee2);
        employeeMap.put("3", employee3);
        employeeMap.put("4", employee4);
        employeeMap.put("5", employee5);
        employeeMap.put("6", employee6);
        employeeMap.put("7", employee7);

        when(employeeDAO.buildEmployeeMapFromCSV("src/main/resources/big_company.csv")).thenReturn(employeeMap);
        employeeService.loadAllEmployee();

        List<CompanyStaff> companyStraffList = employeeService.getLongReportingLine();
        assertEquals(1, companyStraffList.size());
        assertEquals("5", companyStraffList.getFirst().getReportingLineLength());

    }

    @Test
    void loadAllEmployee_returnsCorrectEmployees() throws IOException {
        // Given
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager.Builder()
                        .setId("1")
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setSalary(BigDecimal.valueOf(5000))
                        .setManagerId(null)
                        .build(),
                "2", new Manager.Builder()
                        .setId("2")
                        .setFirstName("Jane")
                        .setLastName("Doe")
                        .setSalary(BigDecimal.valueOf(6000))
                        .setManagerId("1").build());
        when(employeeDAO.buildEmployeeMapFromCSV(anyString())).thenReturn(employeeMap);
        List<CompanyStaff> employees = employeeService.loadAllEmployee();

        assertEquals(2, employees.size());
        Mockito.verify(employeeDAO, times(1)).buildEmployeeMapFromCSV(anyString());
    }

    @Test
    void getReportingLineLength_returnsCorrectLength() throws IOException {
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager.Builder()
                        .setId("1")
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setSalary(BigDecimal.valueOf(5000))
                        .setManagerId(null)
                        .build(),
                "2", new Manager.Builder()
                        .setId("2")
                        .setFirstName("Jane")
                        .setLastName("Doe")
                        .setSalary(BigDecimal.valueOf(6000))
                        .setManagerId("1").build());
        when(employeeDAO.buildEmployeeMapFromCSV(anyString())).thenReturn(employeeMap);
        employeeService.loadAllEmployee();
        int length = employeeService.getReportingLineLength(employee);

        assertEquals(1, length);
    }

    @Test
    void getLongReportingLine_returnsCorrectEmployees() {
        employeeService.loadAllEmployee();
        List<CompanyStaff> employees = employeeService.getLongReportingLine();

        assertTrue(employees.isEmpty());
    }

    @Test
    void printReport_printsCorrectReport() throws UnsupportedEncodingException {
        Map<Manager, BigDecimal> managers = Map.of(
                manager, BigDecimal.valueOf(1000));
        employeeService = new EmployeeService(employeeDAO, salaryService);
        employeeService.printReport("The overpaid managers", managers);

        String expectedOutput = """
                The overpaid managers
                ------------------------------------
                Jane Doe with ID 2 is overpaid by 1000
                """;
        assertEquals(expectedOutput.lines().count(),
                outContent.toString("UTF-8").lines().count());
    }

}