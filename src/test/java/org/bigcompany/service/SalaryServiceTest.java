package org.bigcompany.service;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;
import org.bigcompany.model.Manager;
import org.bigcompany.service.impl.SalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the functionality of the SalaryService class.
 * It includes tests for calculating average subordinate salary,
 * identifying underpaid and overpaid managers.
 *
 * @author Neha B Acharya
 */
class SalaryServiceTest {

    private Manager manager;
    private Employee employee;
    private SalaryService salaryService;

    /**
     * This method sets up the necessary objects for the tests.
     * It creates a manager and a subordinate employee.
     */
    @BeforeEach
    void setUp() {
        salaryService = new SalaryService();
        employee = new Employee("2", "Jane", "Doe", new BigDecimal("4000"), "1");
        manager = new Manager("1", "John", "Doe", new BigDecimal("5000"), null, List.of(employee));
    }

    /**
     * This test checks if the getUnderpaidManagers method correctly identifies
     * underpaid managers and calculates their underpayment amount.
     */
    @Test
    void testGetUnderpaidManagers_returnsUnderpaidManagers() {
        Map<String, CompanyStaff> employees = new HashMap<>();
        employee = new Employee("2", "Jane", "Doe", new BigDecimal("8000"), "1");
        manager = new Manager("1", "John", "Doe", new BigDecimal("5000"), null, List.of(employee));
        employees.put("1", manager);
        employees.put("2", employee);


        Map<Manager, BigDecimal> underpaidManagers = salaryService.getUnderpaidManagers(employees);
        assertTrue(underpaidManagers.containsKey(manager));
        assertEquals(new BigDecimal("4600.00"), underpaidManagers.get(manager));
    }

    /**
     * In this test case, it checks if the method correctly
     * returns an empty map when there are no overpaid managers.
     */

    @Test
    void testGetOverpaidManagers_returnsNoManagers() {
        Map<String, CompanyStaff> employees = new HashMap<String, CompanyStaff>();
        employees.put("1", manager);
        employees.put("2", employee);
        Map<Manager, BigDecimal> overpaidManagers = salaryService.getOverpaidManagers(employees);
        assertTrue(overpaidManagers.isEmpty());
    }

    /**
     * In this test case, it checks if the method correctly identifies
     * overpaid managers and calculates their overpayment amount.
     */

    @Test
    void testGetOverpaidManagers_returnsOverpaidManagers() {
        Map<String, CompanyStaff> employees = new HashMap<String, CompanyStaff>();

        Employee subordinate1 = new Employee("4", "Subordinate", "One", new BigDecimal("4000"), "3");
        Employee subordinate2 = new Employee("5", "Subordinate", "Two", new BigDecimal("5000"), "3");
        Manager overpaidManager = new Manager("3", "Overpaid", "Manager", new BigDecimal("10000"), null, List.of(subordinate1, subordinate2));

        employees.put("3", overpaidManager);
        employees.put("4", subordinate1);
        employees.put("5", subordinate2);

        Map<Manager, BigDecimal> overpaidManagers = salaryService.getOverpaidManagers(employees);
        assertTrue(overpaidManagers.containsKey(overpaidManager));
        assertEquals(new BigDecimal("3250.00"), overpaidManagers.get(overpaidManager));
    }
}