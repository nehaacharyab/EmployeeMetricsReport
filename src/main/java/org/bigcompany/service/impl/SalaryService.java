package org.bigcompany.service.impl;

import org.bigcompany.exception.EmployeeDataException;
import org.bigcompany.exception.InvalidSalaryException;
import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;
import org.bigcompany.service.ISalaryService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The SalaryService class provides methods to manage employee salaries.
 * It provides methods to calculate the average subordinate salary and to get underpaid and overpaid managers.
 * It also provides methods to check if a manager's salary meets a certain condition and to calculate the underpayment or overpayment amount.
 *
 * @author Neha B Acharya
 */
public class SalaryService implements ISalaryService {

    private static final BigDecimal UNDERPAID_MULTIPLIER = BigDecimal.valueOf(1.2);
    private static final BigDecimal OVERPAID_MULTIPLIER = BigDecimal.valueOf(1.5);
    private static final int SCALE = 2;

    /**
     * Gets the underpaid managers.
     * @param employees A map of all employees, keyed by their unique identifiers.
     * @return A map of underpaid managers and their underpayment amounts.
     */
    public Map<Manager, BigDecimal> getUnderpaidManagers(Map<String, CompanyStaff> employees) {
        return getManagersBySalaryCondition(employees, UNDERPAID_MULTIPLIER,
                                            (managerSalary, expectedSalary) -> managerSalary.compareTo(expectedSalary) <
                                                                               0,
                                            UNDERPAID_MULTIPLIER);
    }

    /**
     * Gets the overpaid managers.
     * @param employees A map of all employees, keyed by their unique identifiers.
     * @return A map of overpaid managers and their overpayment amounts.
     */
    public Map<Manager, BigDecimal> getOverpaidManagers(Map<String, CompanyStaff> employees) {
        return getManagersBySalaryCondition(employees,
                                            OVERPAID_MULTIPLIER,
                                            (managerSalary, expectedSalary) -> expectedSalary.compareTo(managerSalary) <
                                                                               0,
                                            OVERPAID_MULTIPLIER);
    }

    /**
     * Calculates the average subordinate salary for a given employee.
     * @param employee The employee to calculate the average subordinate salary for.
     * @return The average subordinate salary for the given employee.
     */
    BigDecimal calculateAverageSubordinateSalary(CompanyStaff employee) {
        try {
            if (!(employee instanceof Manager manager)) {
                throw new EmployeeDataException("CompanyStaff " + employee.getId() + " is not a manager");
            }
            List<CompanyStaff> subordinates = manager.getSubordinates();
            if (subordinates.isEmpty()) {
                return BigDecimal.ZERO;
            }
            BigDecimal totalSalary = subordinates.stream()
                                                 .map(CompanyStaff::getSalary)
                                                 .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalSalary.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidSalaryException("Total salary of subordinates is zero or negative");
            }
            return totalSalary.divide(BigDecimal.valueOf(subordinates.size()), RoundingMode.HALF_UP);
        } catch (EmployeeDataException | InvalidSalaryException e) {
            System.err.println(e.getMessage());
            return BigDecimal.ZERO;
        }
    }


    private Map<Manager, BigDecimal> getManagersBySalaryCondition(Map<String, CompanyStaff> employees,
                                                                  BigDecimal multiplier,
                                                                  BiPredicate<BigDecimal, BigDecimal> salaryComparator,
                                                                  BigDecimal underOrOverPaymentMultiplier) {
        return employees.values().stream()
                        .filter(Manager.class::isInstance)
                        .map(Manager.class::cast)
                        .filter(manager -> isSalaryConditionMet(manager, multiplier, salaryComparator))
                        .collect(Collectors.toMap(Function.identity(),
                                                  manager -> calculateUnderOrOverPayment(manager,
                                                                                         underOrOverPaymentMultiplier)));
    }


    private BigDecimal calculateUnderOrOverPayment(Manager manager, BigDecimal multiplier) {
        BigDecimal averageSubordinateSalary = calculateAverageSubordinateSalary(manager);
        BigDecimal expectedSalary = averageSubordinateSalary.multiply(multiplier);
        BigDecimal managerSalary = manager.getSalary();
        BigDecimal underOrOverPayment = managerSalary.subtract(expectedSalary).abs();
        return underOrOverPayment.setScale(SCALE, RoundingMode.HALF_UP);
    }


    private boolean isSalaryConditionMet(Manager manager,
                                         BigDecimal multiplier,
                                         BiPredicate<BigDecimal, BigDecimal> salaryComparator) {
        BigDecimal averageSubordinateSalary = calculateAverageSubordinateSalary(manager);
        BigDecimal managerSalary = manager.getSalary();
        BigDecimal expectedSalary = averageSubordinateSalary.multiply(multiplier);
        return salaryComparator.test(managerSalary, expectedSalary);
    }

}