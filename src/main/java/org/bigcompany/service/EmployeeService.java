package org.bigcompany.service;

import org.bigcompany.dao.EmployeeDAO;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final SalaryService salaryService;
    private static final String CSV_FILE_PATH = "src/main/resources/big_company.csv";
    private final Map<String, CompanyStaff> employees;

    public EmployeeService(EmployeeDAO employeeDAO, SalaryService salaryService) {
        this.employeeDAO = employeeDAO;
        this.salaryService = salaryService;
        this.employees = new HashMap<>();
    }

    public List<CompanyStaff> loadAllEmployee() {
        List<CompanyStaff> employeeList = new ArrayList<>();
        try {
            Map<String, CompanyStaff> employeeMap = employeeDAO.buildEmployeeMapFromCSV(CSV_FILE_PATH);
            for (CompanyStaff employee : employeeMap.values()) {
                if (isManager(employee.getId(), employeeMap)) {
                    CompanyStaff manager = new Manager.Builder()
                            .setId(employee.getId())
                            .setFirstName(employee.getFirstName())
                            .setLastName(employee.getLastName())
                            .setManagerId(employee.getManagerId())
                            .setSalary(employee.getSalary())
                            .build();
                    employees.put(employee.getId(), manager);
                } else {
                    employees.put(employee.getId(), employee);
                }
            }
            for (CompanyStaff employee : employees.values()) {
                if (employee instanceof Manager manager) {
                    for (CompanyStaff potentialSubordinate : employeeMap.values()) {
                        if (manager.getId().equals(potentialSubordinate.getManagerId())) {
                            manager.addSubordinate(employees.get(potentialSubordinate.getId()));
                        }
                    }
                }
                employeeList.add(employee);
            }
        } catch (IOException exception) {
            System.err.println("Error reading CSV file: " + exception.getMessage());
        }
        return employeeList;
    }


    private boolean isManager(String id, Map<String, CompanyStaff> employeeMap) {
        return employeeMap.values()
                .stream()
                .anyMatch(e -> id.equals(e.getManagerId()));
    }

    public int getReportingLineLength(CompanyStaff employee) {
        int length = 0;
        while (employee.getManagerId() != null) {
            employee = employees.get(employee.getManagerId());
            length++;
        }
        return length;
    }

    public List<CompanyStaff> getLongReportingLine() {
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

    public void generateEmployeeReport() {
        var employeeWithLongReportingLine = getLongReportingLine();
        var overpaidManagers = salaryService.getOverpaidManagers(employees);
        var underpaidManagers = salaryService.getUnderpaidManagers(employees);

        if (!employeeWithLongReportingLine.isEmpty() || !overpaidManagers.isEmpty() || !underpaidManagers.isEmpty()) {
            System.out.println("Employee Report:");
            System.out.println("----------------");
        }

        printEmployeeList("Employee with longer reporting line", employeeWithLongReportingLine);
        printReport("Overpaid Managers", overpaidManagers);
        printReport("Underpaid Managers", underpaidManagers);
    }

    private void printReport(String title, Map<Manager, BigDecimal> managers) {
        if (!managers.isEmpty()) {
            System.out.println(title);
            System.out.println("------------------------------------");
            managers.forEach((manager, payment) ->
                    System.out.println(manager.getFirstName() + " "
                            + manager.getLastName() + " with ID "
                            + manager.getId() + " is " + (title.contains("overpaid") ? "overpaid" : "underpaid") + " by " + payment));
        } else {
            System.out.println("There are no " + title.toLowerCase());
        }
    }

    private void printEmployeeList(String title, List<CompanyStaff> employees) {
        if (!employees.isEmpty()) {
            System.out.println(title);
            System.out.println("------------------------------------");
            employees.stream()
                    .filter(employee -> getReportingLineLength(employee) > 4)
                    .forEach(employee ->
                            System.out.println("CompanyStaff " + employee.getId() + " has a reporting line of length "
                                    + employee.getReportingLineLength()));
        } else {
            System.out.println("There are no " + title.toLowerCase());
        }
    }


}
