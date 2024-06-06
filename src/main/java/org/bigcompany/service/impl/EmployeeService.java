package org.bigcompany.service.impl;

import org.bigcompany.dao.IEmployeeCSVLoader;
import org.bigcompany.dao.impl.EmployeeCSVLoader;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;
import org.bigcompany.service.IEmployeeService;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
/**
 * The EmployeeService class provides methods to load and manage employee data.
 * It uses the EmployeeCSVLoader to load employee data from a CSV file.
 * It also provides a method to determine if an employee is a manager.
 *
 * @author Neha B Acharya
 */
public class EmployeeService implements IEmployeeService {
    private final IEmployeeCSVLoader employeeCSVLoader;
    private Set<String> managerIds;

    private static final String CSV_FILE_PATH = "src/main/resources/big_company_1000_records.csv";

    /**
     * Constructs an EmployeeService with the given EmployeeCSVLoader.
     * @param employeeCSVLoader The loader to use for loading employee data from a CSV file.
     */
    public EmployeeService(EmployeeCSVLoader employeeCSVLoader) {
        this.employeeCSVLoader = employeeCSVLoader;
    }


    /**
     * Loads all employees from a CSV file and organizes them into a map.
     * The map's keys are employee IDs, and the values are CompanyStaff objects.
     * @return A map of all employees, keyed by their unique identifiers.
     */
    public Map<String, CompanyStaff> loadAllEmployee() {
        Map<String, CompanyStaff> employees = new HashMap<>();
        Path filePath = Paths.get(CSV_FILE_PATH);
        Map<String, CompanyStaff> employeeMap = employeeCSVLoader.buildEmployeeMapFromCSV(filePath);
        managerIds = new HashSet<>();
        Map<String, List<CompanyStaff>> managerToSubordinates = new HashMap<>();
        for (CompanyStaff employee : employeeMap.values()) {
            if (isManager(employee.getId(), employeeMap)) {
                Manager manager = new Manager(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getSalary(), employee.getManagerId(), new ArrayList<>());
                employees.put(employee.getId(), manager);
                managerToSubordinates.put(manager.getId(), new ArrayList<>());
            } else {
                employees.put(employee.getId(), employee);
            }
        }

        // Then, assign subordinates to each Manager
        for (CompanyStaff employee : employees.values()) {
            Optional.ofNullable(employee.getManagerId())
                    .ifPresent(managerId -> managerToSubordinates.get(managerId).add(employee));
        }
        // Finally, create new Manager objects with the correct subordinates
        for (Map.Entry<String, List<CompanyStaff>> entry : managerToSubordinates.entrySet()) {
            Manager manager = (Manager) employees.get(entry.getKey());
            Manager updatedManager = manager.withSubordinates(entry.getValue());
            employees.put(entry.getKey(), updatedManager);
        }
        return employees;
    }

    /**
     * Determines if an employee is a manager.
     * An employee is considered a manager if they have at least one subordinate.
     * @param employeeId The ID of the employee to check.
     * @param employeeMap A map of all employees, keyed by their unique identifiers.
     * @return true if the employee is a manager, false otherwise.
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