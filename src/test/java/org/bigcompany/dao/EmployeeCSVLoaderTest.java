package org.bigcompany.dao;

import org.bigcompany.model.CompanyStaff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the EmployeeCSVLoader class.
 * It tests the method buildEmployeeMapFromCSV with different CSV files.
 *
 * @author Neha B Acharya
 */
class EmployeeCSVLoaderTest {

    // Paths to various test CSV files
    private static final String VALID_CSV = "src/test/resources/big_company_valid.csv";
    private static final String EMPTY_CSV = "src/test/resources/big_company_empty.csv";
    private static final String HEADER_ONLY_CSV = "src/test/resources/big_company_header_only.csv";
    private static final String DUPLICATE_IDS_CSV = "src/test/resources/big_company_duplicates.csv";
    private static final String INVALID_SALARY_CSV = "src/test/resources/big_company_malformed_salary.csv";
    private static final String INVALID_NAME_CSV = "src/test/resources/big_company_malformed_name.csv";
    private static final String MULTIPLE_CEOS_CSV = "src/test/resources/big_company_two_ceo.csv";
    private static final String NEGATIVE_SALARY_CSV = "src/test/resources/big_company_neg_sal.csv";
    private static final String ZERO_SALARY_CSV = "src/test/resources/big_company_zero_sal.csv";
    private static final String WRONG_PATH_TO_FILE = "wrong/path/to/file.csv";
    private static final String TEST_EMP_ID = "100";

    // Instance of the class under test
    private EmployeeCSVLoader employeeCSVLoader;

    /**
     * This method is executed before each test.
     * It initializes the EmployeeCSVLoader instance.
     */
    @BeforeEach
    void setUp() {
        employeeCSVLoader = new EmployeeCSVLoader();
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method correctly parses a valid CSV file.
     * It checks that the returned map is not null, not empty, and contains the correct data.
     */
    @Test
    void testBuildEmployeeMapFromCSV_ValidCSV_ReturnsCorrectMap() throws IOException {
        Map<String, CompanyStaff> employeeMap = employeeCSVLoader.buildEmployeeMapFromCSV(VALID_CSV);
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

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IOException when given a wrong file path.
     */
    @Test
    void testBuildEmployeeMapFromCSV_WrongPath_ThrowsException()  {
        assertExceptionForCSVPath(WRONG_PATH_TO_FILE, IOException.class);
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given an empty CSV file.
     */
    @Test
    void testBuildEmployeeMapFromCSV_EmptyCSV_ThrowsException() {
        assertExceptionForCSVPath(EMPTY_CSV, IllegalArgumentException.class);
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method returns an empty map when given a CSV file with only a header.
     */
    @Test
    void testBuildEmployeeMapFromCSV_OnlyHeader_ReturnsEmptyEmployeeMap() throws IOException {
        Map<String, CompanyStaff> employeeMapFromCSV = employeeCSVLoader.buildEmployeeMapFromCSV(HEADER_ONLY_CSV);
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given a CSV file with duplicate IDs.
     */
    @Test
    void testBuildEmployeeMapFromCSV_DuplicateIds_ThrowsException() {
        assertExceptionForCSVPath(DUPLICATE_IDS_CSV, IllegalArgumentException.class);
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given a CSV file with an invalid salary.
     */
    @Test
    void testBuildEmployeeMapFromCSV_InvalidSalary_ThrowsException() {
        assertExceptionForCSVPath(INVALID_SALARY_CSV, IllegalArgumentException.class);
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given a CSV file with an invalid name.
     */
    @Test
    void testBuildEmployeeMapFromCSV_InvalidName_ThrowsException() {
        assertExceptionForCSVPath(INVALID_NAME_CSV, IllegalArgumentException.class);
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given a CSV file with multiple CEOs.
     */
    @Test
    void testBuildEmployeeMapFromCSV_MultipleCeos_ThrowsException() {
        assertExceptionForCSVPath(MULTIPLE_CEOS_CSV, IllegalArgumentException.class);
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given a CSV file with a negative salary.
     */
    @Test
    void testBuildEmployeeMapFromCSV_NegativeSalary_ThrowsException() {
        assertExceptionForCSVPath(NEGATIVE_SALARY_CSV, IllegalArgumentException.class);
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given a CSV file with a zero salary.
     */
    @Test
    void testBuildEmployeeMapFromCSV_ZeroSalary_ThrowsException() {
        assertExceptionForCSVPath(ZERO_SALARY_CSV, IllegalArgumentException.class);
    }

    /**
     * This helper method asserts that an exception of the expected type is thrown when
     * the buildEmployeeMapFromCSV method of the EmployeeCSVLoader class is called with the provided CSV file path.
     *
     * @param csvFilePath The path to the CSV file to be tested.
     * @param expectedException The class of the exception expected to be thrown.
     */
    private void assertExceptionForCSVPath(String csvFilePath, Class<? extends Exception> expectedException) {
        assertThrows(expectedException, () -> employeeCSVLoader.buildEmployeeMapFromCSV(csvFilePath));
    }

}