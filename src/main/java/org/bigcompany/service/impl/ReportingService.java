package org.bigcompany.service.impl;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;
import org.bigcompany.service.IEmployeeService;
import org.bigcompany.service.IReportingService;
import org.bigcompany.service.ISalaryService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * The ReportingService class provides methods to generate reports about employees.
 * It uses the IEmployeeService to load employee data and the ISalaryService to manage employee salaries.
 * It also provides methods to get the length of the reporting line for each employee and to print reports.
 *
 * @author Neha B Acharya
 */
public class ReportingService implements IReportingService {

    private static final int LONG_REPORTING_LINE_THRESHOLD = 4;
    private final IEmployeeService employeeService;
    private final ISalaryService salaryService;
    private Map<String, CompanyStaff> employees = new HashMap<>();


    /**
     * Constructs a ReportingService with the given IEmployeeService and ISalaryService.
     * @param employeeService The service to use for loading employee data.
     * @param salaryService The service to use for managing employee salaries.
     */
    public ReportingService(IEmployeeService employeeService, ISalaryService salaryService) {
        this.employeeService = employeeService;
        this.salaryService = salaryService;
    }


    /**
     * Generates a report about employees with a long reporting line, overpaid managers, and underpaid managers.
     */
    public void generateEmployeeReport() {
        Map<CompanyStaff, Integer> employeeWithLongReportingLine = getEmployeeReportingLineLengths();
        Map<Manager, BigDecimal> overpaidManagers = salaryService.getOverpaidManagers(employees);
        Map<Manager, BigDecimal> underpaidManagers = salaryService.getUnderpaidManagers(employees);

        if (!employeeWithLongReportingLine.isEmpty() || !overpaidManagers.isEmpty() || !underpaidManagers.isEmpty()) {
            System.out.println("\nEmployee Report:");
            System.out.println("----------------");
        }

        printLongReportingLineReport(employeeWithLongReportingLine);
        printPaymentReport("\nThe overpaid managers", overpaidManagers);
        printPaymentReport("\nThe underpaid managers", underpaidManagers);
    }


    /**
     * Gets the length of the reporting line for a given employee.
     * @param employee The employee to get the reporting line length for.
     * @return The length of the reporting line for the given employee.
     */
    int getReportingLineLength(CompanyStaff employee) {
        int length = 0;
        while (employee.getManagerId() != null) {
            employee = employees.get(employee.getManagerId());
            length++;
        }
        return length;
    }

    /**
     * Gets the lengths of the reporting lines for all employees.
     * @return A map of employees and their reporting line lengths.
     */
    public Map<CompanyStaff, Integer> getEmployeeReportingLineLengths() {
        Map<CompanyStaff, Integer> employeeReportingLineLengths = new HashMap<>();
        employees = employeeService.loadAllEmployee();

        for (CompanyStaff employee : employees.values()) {
            int length = getReportingLineLength(employee);
            if(length > LONG_REPORTING_LINE_THRESHOLD)
                employeeReportingLineLengths.put(employee, length);
        }

        return employeeReportingLineLengths;
    }


    /**
     * Prints a report about overpaid or underpaid managers.
     * @param title The title of the report.
     * @param managers A map of managers and their overpayment or underpayment amounts.
     */
    public void printPaymentReport(String title, Map<Manager, BigDecimal> managers) {
        if (!managers.isEmpty()) {
            System.out.println(title);
            System.out.println("------------------------------------");
            managers.forEach((manager, payment) ->
                                     System.out.printf("%s %s with ID %s is %s by %s%n", manager.getFirstName(),
                                                       manager.getLastName(), manager.getId(), title.contains("overpaid") ? "overpaid" : "underpaid", payment));
        } else {
            System.out.println("\nThere are no " + title.toLowerCase());
            System.out.println("-----------------------------------");
        }
    }


    /**
     * Prints a report about employees with a long reporting line.
     * @param employees A map of employees and their reporting line lengths.
     */
    private void printLongReportingLineReport(Map<CompanyStaff, Integer> employees) {
        String title = "\nEmployee with longer reporting line";
        if (!employees.isEmpty()) {
            System.out.println(title);
            System.out.println("------------------------------------");
            employees.forEach((employee, reportingLineLength) ->
                                      System.out.printf("%s %s with ID %s has a reporting line of length %s%n",
                                                        employee.getFirstName(), employee.getLastName(), employee.getId(), reportingLineLength)
            );
        } else {
            System.out.println("There are no " + title.toLowerCase());
        }

    }
}

