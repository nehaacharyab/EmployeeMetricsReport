package org.bigcompany.service;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Employee;
import org.bigcompany.model.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalaryServiceTest {

    private Manager manager;
    private Employee subordinate;
    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        salaryService = new SalaryService();
        manager = new Manager.Builder()
                .setId("1")
                .setFirstName("John")
                .setLastName("Doe")
                .setSalary(new BigDecimal("5000"))
                .setManagerId(null)
                .build();

        subordinate = new Employee.Builder()
                .setId("2")
                .setFirstName("Jane")
                .setLastName("Doe")
                .setSalary(new BigDecimal("4000"))
                .setManagerId("1")
                .build();

        manager.addSubordinate(subordinate);
    }

    @Test
    void calculateAverageSubordinateSalary_returnsCorrectAverage() {
        BigDecimal averageSalary = salaryService.calculateAverageSubordinateSalary(manager);
        assertEquals(new BigDecimal("4000"), averageSalary);
    }

    @Test
    void calculateAverageSubordinateSalary_throwsExceptionForNonManager() {
        assertThrows(IllegalArgumentException.class,
                () -> salaryService.calculateAverageSubordinateSalary(subordinate));
    }

    @Test
    void getUnderpaidManagers_returnsCorrectManagers() {
        var employees = new HashMap<String, CompanyStaff>();
        subordinate = new Employee.Builder()
                .setId("2")
                .setFirstName("Jane")
                .setLastName("Doe")
                .setSalary(new BigDecimal("8000"))
                .setManagerId("1")
                .build();
        manager.addSubordinate(subordinate);
        employees.put("1", manager);
        employees.put("2", subordinate);

        Map<Manager, BigDecimal> underpaidManagers = salaryService.getUnderpaidManagers(employees);
        assertTrue(underpaidManagers.containsKey(manager));
        assertEquals(new BigDecimal("2200.00"), underpaidManagers.get(manager));
    }

    @Test
    void getOverpaidManagers_returnsCorrectManagers() {
        var employees = new HashMap<String, CompanyStaff>();
        employees.put("1", manager);
        employees.put("2", subordinate);
        manager.addSubordinate(subordinate);
        Map<Manager, BigDecimal> overpaidManagers = salaryService.getOverpaidManagers(employees);
        assertTrue(overpaidManagers.isEmpty());
    }
}