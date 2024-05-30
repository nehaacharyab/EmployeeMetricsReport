package org.bigcompany;

import org.bigcompany.dao.EmployeeCSVLoader;
import org.bigcompany.service.EmployeeService;
import org.bigcompany.service.SalaryService;

/**
 * This is the main class for generating the employee report.
 * It creates instances of EmployeeCSVLoader, SalaryService, and EmployeeService.
 * Then it calls the generateEmployeeReport method of the EmployeeService class.
 *
 * @author Neha B Acharya
 */
public class ReportGenerator {

    /**
     * The main method which is the entry point of the application.
     * It creates instances of EmployeeCSVLoader, SalaryService, and EmployeeService.
     * Then it calls the generateEmployeeReport method of the EmployeeService class.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        EmployeeCSVLoader employeeDAO = new EmployeeCSVLoader();
        SalaryService salaryService = new SalaryService();
        EmployeeService employeeService = new EmployeeService(employeeDAO, salaryService);
        employeeService.generateEmployeeReport();
    }
}
