package org.bigcompany;

import org.bigcompany.dao.EmployeeCSVLoader;
import org.bigcompany.service.EmployeeService;
import org.bigcompany.service.SalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * This class is used to test the functionality of the EmployeeService class.
 * It uses Mockito to mock the dependencies of the EmployeeService class.
 *
 * @author Neha B Acharya
 */
class ReportGeneratorTest {
    private EmployeeCSVLoader employeeDAO;
    private SalaryService salaryService;
    private EmployeeService employeeService;

    /**
     * This method is executed before each test. It initializes the mock objects and the EmployeeService object.
     */
    @BeforeEach
    public void setup() {
        employeeDAO = Mockito.mock(EmployeeCSVLoader.class);
        salaryService = Mockito.mock(SalaryService.class);
        employeeService = new EmployeeService(employeeDAO, salaryService);
    }

    /**
     * This test verifies that the getOverpaidManagers and getUnderpaidManagers methods of the SalaryService class
     * are called when the generateEmployeeReport method of the EmployeeService class is called.
     */
    @Test
    void testGenerateEmployeeReport_callsSalaryService() {
        employeeService.generateEmployeeReport();
        verify(salaryService, times(1))
                .getOverpaidManagers(any());
        verify(salaryService, times(1))
                .getUnderpaidManagers(any());
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method of the EmployeeCSVLoader class is called when the
     * generateEmployeeReport method of the EmployeeService class is called.
     */
    @Test
    void testGenerateEmployeeReport_callsEmployeeDAO() throws IOException {
        employeeService.generateEmployeeReport();
        verify(employeeDAO, times(1))
                .buildEmployeeMapFromCSV(anyString());
    }
}