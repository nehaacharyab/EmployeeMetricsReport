package org.bigcompany;

import org.bigcompany.dao.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;
import org.bigcompany.service.EmployeeService;
import org.bigcompany.service.SalaryService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is used to test the functionality of the EmployeeService class.
 *
 * @author Neha B Acharya
 */
class ReportGeneratorTest {

    /**
     * This test verifies that the getOverpaidManagers and getUnderpaidManagers methods of the SalaryService class
     * are called when the generateEmployeeReport method of the EmployeeService class is called.
     */
    @Test
    void testGenerateEmployeeReport_callsSalaryService() {
        SalaryServiceStub salaryServiceStub  = new SalaryServiceStub();
        EmployeeCSVLoaderStub employeeCSVLoader = new EmployeeCSVLoaderStub();
        EmployeeService employeeService = new EmployeeService(employeeCSVLoader, salaryServiceStub);
        employeeService.generateEmployeeReport();
        assertEquals(1, salaryServiceStub.getOverpaidManagersInvocationCount());
        assertEquals(1, salaryServiceStub.getUnderpaidManagersInvocationCount());
    }

    /**
     * This test verifies that the buildEmployeeMapFromCSV method of the EmployeeCSVLoader class is called when the
     * generateEmployeeReport method of the EmployeeService class is called.
     */
    @Test
    void testGenerateEmployeeReport_callsEmployeeCSVLoader() {
        EmployeeCSVLoaderStub employeeCSVLoaderStub = new EmployeeCSVLoaderStub();
        SalaryServiceStub salaryServiceStub = new SalaryServiceStub();
        EmployeeService employeeService = new EmployeeService(employeeCSVLoaderStub, salaryServiceStub);
        employeeService.generateEmployeeReport();
        assertEquals(1, employeeCSVLoaderStub.buildEmployeeMapFromCSVInvocation());
    }

    static class EmployeeCSVLoaderStub extends EmployeeCSVLoader{
        int invocationCount = 0;

        @Override
        public Map<String, CompanyStaff> buildEmployeeMapFromCSV(String csvFilePath) {
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