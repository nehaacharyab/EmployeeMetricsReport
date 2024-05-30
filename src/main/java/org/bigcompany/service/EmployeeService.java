package org.bigcompany.service;

import org.bigcompany.dao.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * This class provides services related to employees, such as loading employees from a CSV file, calculating the
 * reporting line length for a given employee, retrieving a list of employees with a reporting line length greater than 4,
 * and generating a report of employees with a long reporting line, overpaid managers, and underpaid managers.
 *
 * @author Neha B Acharya
 */
public class EmployeeService {
    private static final String CSV_FILE_PATH = "src/main/resources/big_company_1000_records.csv";
    private final EmployeeCSVLoader employeeCSVLoader;
    private final SalaryService salaryService;
    private final Map<String, CompanyStaff> employees = new HashMap<>();
    private Set<String> managerIds;

    /**
     * Constructs an EmployeeService with the given EmployeeCSVLoader and SalaryService.
     *
     * @param employeeCSVLoader
     *         the EmployeeCSVLoader to use for loading employee data
     * @param salaryService
     *         the SalaryService to use for calculating salaries
     */
    public EmployeeService(EmployeeCSVLoader employeeCSVLoader, SalaryService salaryService) {
        this.employeeCSVLoader = employeeCSVLoader;
        this.salaryService = salaryService;
    }

    /**
     * Generates a report of employees with a long reporting line, overpaid managers, and underpaid managers.
     */
    public void generateEmployeeReport() {
        List<CompanyStaff> employeeWithLongReportingLine = getLongReportingLine();
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
     * Loads all employees from a CSV file. If the employees have already been loaded, it returns the already loaded
     * employees. Otherwise, it loads the employees from the CSV file, determines which employees are managers, and
     * assigns subordinates to each manager. If an error occurs while reading the CSV file, an error message is printed
     * to the console and an empty list is returned.
     *
     * @return a list of all employees
     */
    List<CompanyStaff> loadAllEmployee() {
        if (!employees.isEmpty()) {
            return new ArrayList<>(employees.values());
        }
        try {
            Map<String, CompanyStaff> employeeMap = employeeCSVLoader.buildEmployeeMapFromCSV(CSV_FILE_PATH);
            managerIds = new HashSet<>();
            for (CompanyStaff employee : employeeMap.values()) {
                if (isManager(employee.getId(), employeeMap)) {
                    Manager manager = new Manager(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getSalary(), employee.getManagerId());
                    employees.put(employee.getId(), manager);
                } else {
                    employees.put(employee.getId(), employee);
                }
            }
            assignSubordinates(employeeMap);
        } catch (IOException exception) {
            System.err.println("Error reading CSV file: " + exception.getMessage());
        }
        return new ArrayList<>(employees.values());
    }

    /**
     * Calculates the reporting line length for a given employee. The reporting line length is defined as the number of
     * managers above the employee in the hierarchy.
     *
     * @param employee the employee for whom to calculate the reporting line length
     * @return the reporting line length of the employee
     */
    int getReportingLineLength(CompanyStaff employee) {
        int length = 0;
        while (employee.getManagerId() != null) {
            employee = employees.get(employee.getManagerId());
            length++;
        }
        return length - 1;
    }

    /**
     * Retrieves a list of employees with a reporting line length greater than 4.
     *
     * @return a list of employees with a long reporting line
     */
    List<CompanyStaff> getLongReportingLine() {
        return loadAllEmployee().stream()
                .filter(employee -> {
                    int length = getReportingLineLength(employee);
                    if (length > 4) {
                        employee.setReportingLineLength(String.valueOf(length));
                        return true;
                    }
                    return false;
                }).toList();
    }

    /**
     * Prints a report for a given set of managers and their overpayment or underpayment amounts to the console.
     *
     * @param title the title of the report
     * @param managers a map of managers to their overpayment or underpayment amounts
     */
    void printPaymentReport(String title, Map<Manager, BigDecimal> managers) {
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
     * Prints a list of employees with a reporting line length greater than 4 to the console.
     *
     * @param employees the list of employees to print
     */
    private void printLongReportingLineReport(List<CompanyStaff> employees) {
        String title = "\nEmployee with longer reporting line";
        if (!employees.isEmpty()) {
            System.out.println(title);
            System.out.println("------------------------------------");
            employees.stream()
                    .filter(employee -> getReportingLineLength(employee) > 4)
                    .forEach(employee ->
                            System.out.printf("%s %s with ID %s has a reporting line of length %s%n",
                                    employee.getFirstName(), employee.getLastName() , employee.getId(), employee.getReportingLineLength())) ;
        } else {
            System.out.println("There are no " + title.toLowerCase());
        }
    }

    /**
     * Assigns subordinates to each manager in the given map of employees. A subordinate is defined as an employee who
     * has the manager as their direct manager.
     *
     * @param employeeMap
     *         a map of employees
     */
    private void assignSubordinates(Map<String, CompanyStaff> employeeMap) {
        for (CompanyStaff employee : employees.values()) {
            if (employee instanceof Manager manager) {
                for (CompanyStaff potentialSubordinate : employeeMap.values()) {
                    if (manager.getId().equals(potentialSubordinate.getManagerId())) {
                        manager.addSubordinate(employees.get(potentialSubordinate.getId()));
                    }
                }
            }
        }
    }

    /**
     * Checks if the given employee ID belongs to a manager. It first checks if the ID is in the managerIds set. If it's
     * not, it checks if any employee has the given ID as their manager. If an employee is found, the ID is added to the
     * managerIds set.
     *
     * @param employeeId
     *         the ID of the employee to check
     * @param employeeMap
     *         a map of all employees
     * @return true if the given ID belongs to a manager, false otherwise
     */
    private boolean isManager(String employeeId, Map<String, CompanyStaff> employeeMap) {
        if (managerIds.contains(employeeId)) {
            return true;
        }
        boolean isManager = employeeMap.values()
                .stream()
                .anyMatch(e -> employeeId.equals(e.getManagerId()));
        if (isManager) {
            managerIds.add(employeeId);
        }
        return isManager;
    }

}
