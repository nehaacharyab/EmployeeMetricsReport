package org.bigcompany.dao;

import org.bigcompany.model.CompanyStaff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private static final String VALID_CSV = "src/test/resources/big_company_valid.csv";
    private static final String EMPTY_CSV = "src/test/resources/empty.csv";
    private static final String HEADER_ONLY_CSV = "src/test/resources/header_only_test.csv";
    private static final String DUPLICATE_IDS_CSV = "src/test/resources/big_company_duplicates.csv";
    private static final String INVALID_SALARY_CSV = "src/test/resources/big_company_malformed_salary_test.csv";
    private static final String INVALID_NAME_CSV = "src/test/resources/big_company_malformed_name_test.csv";
    private static final String MULTIPLE_CEOS_CSV = "src/test/resources/big_company_two_ceo.csv";
    private static final String NEGATIVE_SALARY_CSV = "src/test/resources/big_company_neg_sal.csv";
    private static final String ZERO_SALARY_CSV = "src/test/resources/big_company_zero_sal.csv";
    private static final String WRONG_PATH_TO_FILE = "wrong/path/to/file.csv";
    private static final String TEST_EMP_ID = "100";

    private EmployeeDAO employeeDAO;

    @BeforeEach
    void setUp() {
        employeeDAO = new EmployeeDAO();
    }

    @Test
    void testBuildEmployeeMapFromCSV_ValidCSV_ReturnsCorrectMap() throws IOException {
        Map<String, CompanyStaff> employeeMap = employeeDAO.buildEmployeeMapFromCSV(VALID_CSV);
        Set<String> setOfEmployeeIds = employeeMap.keySet();
        CompanyStaff companyStaff = employeeMap.get(TEST_EMP_ID);

        assertNotNull(employeeMap, "Employee map should not be null");
        assertFalse(employeeMap.isEmpty(), "Employee map should not be empty");
        assertNotNull(companyStaff, "Employee with ID 100 should exist");
        assertEquals("Karina", companyStaff.getFirstName(), "First name should be Karina");
        assertEquals("Cloris", companyStaff.getLastName(), "Last name should be Cloris");
        assertEquals(2, setOfEmployeeIds.size(), "There must be 10 employees");
        assertNotNull(companyStaff.getSalary(), "Salary should not be null");
        assertTrue(companyStaff.getSalary().compareTo(BigDecimal.ZERO) > 0, "Salary should be greater than zero");
    }

    @Test
    void testBuildEmployeeMapFromCSV_WrongPath_ThrowsException()  {
        assertThrows(IOException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(WRONG_PATH_TO_FILE));
    }

    @Test
    void testBuildEmployeeMapFromCSV_EmptyCSV_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(EMPTY_CSV));
    }

    @Test
    void testBuildEmployeeMapFromCSV_OnlyHeader_ReturnsEmptyEmployeeMap() throws IOException {
        var employeeMapFromCSV = employeeDAO.buildEmployeeMapFromCSV(HEADER_ONLY_CSV);
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    @Test
    void testBuildEmployeeMapFromCSV_DuplicateIds_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(DUPLICATE_IDS_CSV));
    }

    @Test
    void testBuildEmployeeMapFromCSV_InvalidSalary_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(INVALID_SALARY_CSV));
    }

    @Test
    void testBuildEmployeeMapFromCSV_InvalidName_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(INVALID_NAME_CSV));
    }

    @Test
    void testBuildEmployeeMapFromCSV_MultipleCeos_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(MULTIPLE_CEOS_CSV));
    }

    @Test
    void testBuildEmployeeMapFromCSV_NegativeSalary_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(NEGATIVE_SALARY_CSV));
    }

    @Test
    void testBuildEmployeeMapFromCSV_ZeroSalary_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> employeeDAO.buildEmployeeMapFromCSV(ZERO_SALARY_CSV));
    }
}