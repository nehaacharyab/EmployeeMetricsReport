package org.bigcompany;

import org.bigcompany.dao.impl.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;
import org.bigcompany.service.ISalaryService;
import org.bigcompany.service.factory.ServiceFactory;
import org.bigcompany.service.impl.EmployeeService;
import org.bigcompany.service.impl.ReportingService;
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
        EmployeeService employeeService = new EmployeeService(employeeCSVLoader);
        ReportingService reportingService = new ReportingService(employeeService, salaryServiceStub);
        reportingService.generateEmployeeReport();
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
        EmployeeService employeeService = new EmployeeService(employeeCSVLoaderStub);
        ReportingService reportingService = new ReportingService(employeeService, salaryServiceStub);
        reportingService.generateEmployeeReport();
        assertEquals(1, employeeCSVLoaderStub.buildEmployeeMapFromCSVInvocation());
    }

    /**
     * This test verifies that the main method of the ReportGenerator class calls the buildEmployeeMapFromCSV method
     * of the EmployeeCSVLoader class.
     */
    @Test
    void testMainMethod() {
        EmployeeCSVLoaderStub employeeCSVLoaderStub = new EmployeeCSVLoaderStub();
        SalaryServiceStub salaryServiceStub = new SalaryServiceStub();
        EmployeeService employeeService = new EmployeeService(employeeCSVLoaderStub);

        ServiceFactory.setEmployeeService(employeeService);
        ServiceFactory.setSalaryService(salaryServiceStub);

        ReportGenerator.main(new String[]{});

        assertEquals(1, employeeCSVLoaderStub.buildEmployeeMapFromCSVInvocation());
    }

    /**
     * This class is used to test the functionality of the EmployeeCSVLoader class.
     */
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

    /**
     * This class is used to test the functionality of the SalaryService class.
     */
    static class SalaryServiceStub implements ISalaryService{
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