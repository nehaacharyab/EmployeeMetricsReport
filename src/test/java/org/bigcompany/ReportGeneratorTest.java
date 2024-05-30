package org.bigcompany;

import org.bigcompany.dao.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;
import org.bigcompany.service.EmployeeService;
import org.bigcompany.service.SalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is used to test the functionality of the EmployeeService class.
 * It uses Mockito to mock the dependencies of the EmployeeService class.
 *
 * @author Neha B Acharya
 */
class ReportGeneratorTest {
    private EmployeeCSVLoader employeeCSVLoader;
    private SalaryService salaryService;
    private EmployeeService employeeService;

    /**
     * This method is executed before each test. It initializes the mock objects and the EmployeeService object.
     */
    @BeforeEach
    public void setup() {
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);
    }

    /**
     * This test verifies that the getOverpaidManagers and getUnderpaidManagers methods of the SalaryService class
     * are called when the generateEmployeeReport method of the EmployeeService class is called.
     */
    @Test
    void testGenerateEmployeeReport_callsSalaryService() {
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager("1", "John", "Doe", new BigDecimal("5000"), null),
                "2", new Manager("2", "Jane", "Doe", new BigDecimal("6000"), "1"));
        SalaryServiceStub salaryService = new SalaryServiceStub();
        employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);
        employeeService.generateEmployeeReport();
        assertEquals(1, salaryService.getOverpaidManagersInvocationCount());
        assertEquals(1, salaryService.getUnderpaidManagersInvocationCount());
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method of the EmployeeCSVLoader class is called when the
     * generateEmployeeReport method of the EmployeeService class is called.
     */
    @Test
    void testGenerateEmployeeReport_callsEmployeeCSVLoader() {
        Map<String, CompanyStaff> employeeMap = Map.of(
                "1", new Manager("1", "John", "Doe", new BigDecimal("5000"), null),
                "2", new Manager("2", "Jane", "Doe", new BigDecimal("6000"), "1"));
        EmployeeCSVLoaderStub employeeCSVLoader = new EmployeeCSVLoaderStub(employeeMap);
        SalaryServiceStub salaryService = new SalaryServiceStub();
        employeeService = new EmployeeService(employeeCSVLoader, salaryService);
        employeeService.generateEmployeeReport();
        assertEquals(1, employeeCSVLoader.buildEmployeeMapFromCSVInvocation());
    }

    static class EmployeeCSVLoaderStub extends EmployeeCSVLoader{
        int invocationCount = 0;

        EmployeeCSVLoaderStub(Map<String, CompanyStaff> employeeMap){
        }
        @Override
        public Map<String, CompanyStaff> buildEmployeeMapFromCSV(String csvFilePath) throws IOException {
            invocationCount++;
            return Map.of();
        }

        public int buildEmployeeMapFromCSVInvocation(){
            return invocationCount;
        }
    }

    static class SalaryServiceStub extends SalaryService{
        private int overpaidInvocationCount = 0;
        private int underpaidInvocationCount = 0;

        @Override
        public Map<Manager, BigDecimal> getOverpaidManagers(Map<String, CompanyStaff> employees) {
            overpaidInvocationCount++;
            return Map.of();
        }

        @Override
        public Map<Manager, BigDecimal> getUnderpaidManagers(Map<String, CompanyStaff> employees) {
            underpaidInvocationCount++;
            return Map.of();
        }

        public int getOverpaidManagersInvocationCount() {
            return overpaidInvocationCount;
        }

        public int getUnderpaidManagersInvocationCount() {
            return underpaidInvocationCount;
        }
    }
}