package org.bigcompany;

import org.bigcompany.exception.EmployeeDataException;
import org.bigcompany.service.IEmployeeService;
import org.bigcompany.service.IReportingService;
import org.bigcompany.service.ISalaryService;
import org.bigcompany.service.factory.ServiceFactory;
import org.bigcompany.service.impl.ReportingService;

/**
 * The ReportGenerator class is the main entry point for generating the employee report.
 *
 * @author Neha B Acharya
 */
public class ReportGenerator {

    /**
     * The main method which is the entry point of the application.
     * It creates instances of EmployeeService, SalaryService, and ReportingService.
     * Then it calls the generateEmployeeReport method of the ReportingService class.
     * It catches any exceptions that occur and prints the error message.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            IEmployeeService employeeService = ServiceFactory.createEmployeeService();
            ISalaryService salaryService = ServiceFactory.createSalaryService();
            IReportingService reportingService = new ReportingService(employeeService, salaryService);
            reportingService.generateEmployeeReport();
        } catch (EmployeeDataException e) {
            System.err.println("An error occurred while loading employee data: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
