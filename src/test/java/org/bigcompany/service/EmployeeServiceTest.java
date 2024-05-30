package org.bigcompany.service;

import org.bigcompany.dao.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;
import org.bigcompany.model.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * This class tests the functionality of the EmployeeService class.
 * It uses Mockito to mock dependencies and JUnit for assertions.
 *
 * @author Neha B Acharya
 */
class EmployeeServiceTest {
    private static final String EMPTY_CSV = "src/test/resources/big_company_empty.csv";
    private static final String HEADER_ONLY_CSV = "src/test/resources/big_company_header_only.csv";
    private static final String DUPLICATE_IDS_CSV = "src/test/resources/big_company_duplicates.csv";
    private static final String INVALID_SALARY_CSV = "src/test/resources/big_company_malformed_salary.csv";
    private static final String INVALID_NAME_CSV = "src/test/resources/big_company_malformed_name.csv";
    private static final String MULTIPLE_CEOS_CSV = "src/test/resources/big_company_two_ceo.csv";
    private static final String NEGATIVE_SALARY_CSV = "src/test/resources/big_company_neg_sal.csv";
    private static final String ZERO_SALARY_CSV = "src/test/resources/big_company_zero_sal.csv";
    private static final String WRONG_PATH_TO_FILE = "wrong/path/to/file.csv";

    private EmployeeService employeeService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Employee employee;
    private Manager manager;

    @Mock
    private EmployeeCSVLoader employeeDAO;
    @Mock
    private SalaryService salaryService;


    /**
     * This method is executed before each test. It initializes the mocks and the EmployeeService instance.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        employeeService = new EmployeeService(employeeDAO, salaryService);
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
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file is empty.
     */
    @Test
    void loadAllEmployeeWithEmptyCsvShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(EMPTY_CSV);
    }

    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file path is wrong.
     */
    @Test
    void loadAllEmployeeWithWrongPathShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(WRONG_PATH_TO_FILE);
    }

    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file only contains a header.
     */
    @Test
    void loadAllEmployeeWithOnlyHeaderShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(HEADER_ONLY_CSV);
    }

    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file contains duplicate IDs.
     */
    @Test
    void loadAllEmployeeWithDuplicateIdsShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(DUPLICATE_IDS_CSV);
    }


    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file contains an invalid salary.
     */
    @Test
    void loadAllEmployeeWithInvalidSalaryShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(INVALID_SALARY_CSV);
    }

    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file contains an invalid name.
     */
    @Test
    void loadAllEmployeeWithInvalidNameShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(INVALID_NAME_CSV);
    }


    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file contains multiple CEOs.
     */
    @Test
    void loadAllEmployeeWithMultipleCeoShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(MULTIPLE_CEOS_CSV);
    }


    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file contains a negative salary.
     */
    @Test
    void loadAllEmployeeWithNegativeSalaryShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(NEGATIVE_SALARY_CSV);
    }


    /**
     * This test verifies that the loadAllEmployee method returns an empty list when the CSV file contains a zero salary.
     */
    @Test
    void loadAllEmployeeWithZeroSalaryShouldReturnEmptyList() throws IOException {
        loadAllEmployeeAndAssertEmpty(ZERO_SALARY_CSV);
    }

    /**
     * This test verifies that the loadAllEmployee method returns the correct employees
     * when one employee is a manager and the other a normal employee.
     */
    @Test
    void testLoadAllEmployeeShouldReturnCorrectEmployees() throws IOException {
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

    /**
     * This test verifies that the getLongReportingLine method returns the correct employees.
     */
    @Test
    void testGetLongReportingLineShouldReturnCorrectEmployees() throws IOException {
        Map<String, CompanyStaff> employeeMap = new HashMap<>();
        employeeMap.put("1", new Employee("1", "Karina", "Cloris", new BigDecimal(10000), null));
        employeeMap.put("2", new Employee("2", "Dulcinea", "Greenwald", new BigDecimal(8998), "1"));
        employeeMap.put("3", new Employee("3", "Neha", "Acharya", new BigDecimal(7000), "2"));
        employeeMap.put("4", new Employee("4", "Anica", "Haldas", new BigDecimal(7000), "3"));
        employeeMap.put("5", new Employee("5", "Blondelle", "Greyson", new BigDecimal(7000), "4"));
        employeeMap.put("6", new Employee("6", "Blondelle", "Greyson", new BigDecimal(7000), "5"));
        employeeMap.put("7", new Employee("7", "Blondelle", "Greyson", new BigDecimal(7000), "6"));

        when(employeeDAO.buildEmployeeMapFromCSV(anyString())).thenReturn(employeeMap);
        employeeService.loadAllEmployee();

        List<CompanyStaff> companyStraffList = employeeService.getLongReportingLine();
        assertEquals(1, companyStraffList.size());
        assertEquals("5", companyStraffList.getFirst().getReportingLineLength());

    }

    /**
     * This test verifies that the loadAllEmployee method returns the correct employees
     * when both the manager and the subordinate are managers themselves.
     */
    @Test
    void testLoadAllEmployeeShouldReturnCorrectEmployeesCase2() throws IOException {
        // Given
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager("1", "John", "Doe", new BigDecimal("5000"), null),
                "2", new Manager("2", "Jane", "Doe", new BigDecimal("6000"), "1"));
        when(employeeDAO.buildEmployeeMapFromCSV(anyString())).thenReturn(employeeMap);
        List<CompanyStaff> employees = employeeService.loadAllEmployee();

        assertEquals(2, employees.size());
        verify(employeeDAO, times(1)).buildEmployeeMapFromCSV(anyString());
    }

    /**
     * This test verifies that the getReportingLineLength method returns the correct reporting length.
     */
    @Test
    void testGetReportingLineLengthShouldReturnCorrectLength() throws IOException {
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager("1", "John", "Doe", new BigDecimal("5000"), null),
                "2", new Manager("2", "Jane", "Doe", new BigDecimal("6000"), "1"));
        when(employeeDAO.buildEmployeeMapFromCSV(anyString())).thenReturn(employeeMap);
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
        employeeService = new EmployeeService(employeeDAO, salaryService);
        employeeService.printPaymentReport("The overpaid managers", managers);

        String expectedOutput = """
                The overpaid managers
                ------------------------------------
                Jane Doe with ID 2 is overpaid by 1000
                """;
        assertEquals(expectedOutput.lines().count(),
                outContent.toString(StandardCharsets.UTF_8).lines().count());
    }

    /**
     * This helper method asserts that an IllegalArgumentException is thrown when
     * the buildEmployeeMapFromCSV method of the EmployeeCSVLoader class is called with the provided CSV file path,
     * and verifies that the returned employee map from the loadAllEmployee method of the EmployeeService class is empty.
     *
     * @param csvFilePath The path to the CSV file to be tested.
     * @throws IOException If an input or output exception occurred
     */
    private void loadAllEmployeeAndAssertEmpty(String csvFilePath) throws IOException {
        doThrow(IllegalArgumentException.class).when(employeeDAO)
                .buildEmployeeMapFromCSV(csvFilePath);
        List<CompanyStaff> employeeMapFromCSV = employeeService.loadAllEmployee();
        assertTrue(employeeMapFromCSV.isEmpty());
    }

}