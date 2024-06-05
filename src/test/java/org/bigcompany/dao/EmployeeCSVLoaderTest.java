package org.bigcompany.dao;

import org.bigcompany.dao.impl.EmployeeCSVLoader;
import org.bigcompany.exception.EmployeeDataException;
import org.bigcompany.exception.InvalidSalaryException;
import org.bigcompany.model.CompanyStaff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the functionality of the EmployeeCSVLoader class.
 *
 * @author Neha B Acharya
 */
class EmployeeCSVLoaderTest {

    // Paths to various test CSV files
    private static final String VALID_CSV = String.valueOf(Paths.get("src/test/resources/big_company_valid.csv"));
    private static final String EMPTY_CSV = String.valueOf(Paths.get("src/test/resources/big_company_empty.csv"));
    private static final String HEADER_ONLY_CSV = String.valueOf(Paths.get("src/test/resources/big_company_header_only.csv"));
    private static final String DUPLICATE_IDS_CSV = String.valueOf(Paths.get("src/test/resources/big_company_duplicates.csv"));
    private static final String INVALID_SALARY_CSV = String.valueOf(Paths.get("src/test/resources/big_company_malformed_salary.csv"));
    private static final String INVALID_NAME_CSV = String.valueOf(Paths.get("src/test/resources/big_company_malformed_name.csv"));
    private static final String MULTIPLE_CEOS_CSV = String.valueOf(Paths.get("src/test/resources/big_company_two_ceo.csv"));
    private static final String NEGATIVE_SALARY_CSV = String.valueOf(Paths.get("src/test/resources/big_company_neg_sal.csv"));
    private static final String ZERO_SALARY_CSV = String.valueOf(Paths.get("src/test/resources/big_company_zero_sal.csv"));
    private static final String INCORRECT_FIELDS_CSV = String.valueOf(Paths.get("src/test/resources/big_company_incorrect_fields.csv"));
    private static final String WRONG_PATH_TO_FILE = String.valueOf(Paths.get("wrong/path/to/file.csv"));
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
     * This test verifies that the buildEmployeeMapFromCSV method returns the correct map when given a valid CSV file.
     */
    @Test
    void testBuildEmployeeMapFromCSV_ValidCSV_ReturnsCorrectMap() {
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
     * This test verifies that the buildEmployeeMapFromCSV method returns an empty map when given a non-existent CSV file.
     */
    @Test
    void testBuildEmployeeMapFromCSV_WrongPath_ReturnsEmptyMap() {
        Map<String, CompanyStaff> employeeMap = employeeCSVLoader.buildEmployeeMapFromCSV(WRONG_PATH_TO_FILE);
        assertTrue(employeeMap.isEmpty());
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an EmployeeDataException when given an empty CSV file.
     */
    @Test
    void testBuildEmployeeMapFromCSV_EmptyCSV_ThrowsException() {
        assertExceptionForCSVPath(EMPTY_CSV, EmployeeDataException.class, "The CSV file is empty");
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method returns an empty map when given a CSV file with only a header.
     */
    @Test
    void testBuildEmployeeMapFromCSV_OnlyHeader_ReturnsEmptyEmployeeMap() {
        Map<String, CompanyStaff> employeeMapFromCSV = employeeCSVLoader.buildEmployeeMapFromCSV(HEADER_ONLY_CSV);
        assertTrue(employeeMapFromCSV.isEmpty());
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an EmployeeDataException when given a CSV file with duplicate IDs.
     */
    @Test
    void testBuildEmployeeMapFromCSV_DuplicateIds_ThrowsException() {
        assertExceptionForCSVPath(DUPLICATE_IDS_CSV, EmployeeDataException.class, "Duplicate employee ID");
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an InvalidSalaryException when given a CSV file with an invalid salary.
     */
    @Test
    void testBuildEmployeeMapFromCSV_InvalidSalary_ThrowsException() {
        assertExceptionForCSVPath(INVALID_SALARY_CSV, InvalidSalaryException.class, "Invalid salary:");
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an EmployeeDataException when given a CSV file with an invalid name.
     */
    @Test
    void testBuildEmployeeMapFromCSV_InvalidName_ThrowsException() {
        assertExceptionForCSVPath(INVALID_NAME_CSV, EmployeeDataException.class, "ID, first name, or last name is empty in line");
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an EmployeeDataException when given a CSV file with multiple CEOs.
     */
    @Test
    void testBuildEmployeeMapFromCSV_MultipleCeos_ThrowsException() {
        assertExceptionForCSVPath(MULTIPLE_CEOS_CSV, EmployeeDataException.class, "More than one employee without a manager ID");
    }

   /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an InvalidSalaryException when given a CSV file with a negative salary.
     */
    @Test
    void testBuildEmployeeMapFromCSV_NegativeSalary_ThrowsException() {
        assertExceptionForCSVPath(NEGATIVE_SALARY_CSV, InvalidSalaryException.class, "Salary must be greater than zero");
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an IllegalArgumentException when given a CSV file with a zero salary.
     */
    @Test
    void testBuildEmployeeMapFromCSV_ZeroSalary_ThrowsException() {
        assertExceptionForCSVPath(ZERO_SALARY_CSV, InvalidSalaryException.class, "Salary must be greater than zero");
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method throws an EmployeeDataException when given a CSV file with incorrect fields.
     */
    @Test
    void testBuildEmployeeMapFromCSV_IncorrectFields_ThrowsException() {
        assertExceptionForCSVPath(INCORRECT_FIELDS_CSV, EmployeeDataException.class, "Incorrect number of fields in line: 101,Dulcinea,Greenwald,8998,107,89989");
    }

    /**
     * This method asserts that the buildEmployeeMapFromCSV method throws the expected exception with the expected message
     * @param csvFilePath
     * @param expectedException
     * @param expectedMessage
     */
    private void assertExceptionForCSVPath(String csvFilePath, Class<? extends Exception> expectedException, String expectedMessage) {
        Exception exception = assertThrows(expectedException,
                () -> employeeCSVLoader.buildEmployeeMapFromCSV(csvFilePath));
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}