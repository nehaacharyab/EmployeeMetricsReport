package org.bigcompany.service;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Service class for salary-related calculations within the company.
 * Provides methods to analyze manager salaries, identify underpaid and overpaid managers,
 * and calculate average subordinate salaries.
 *
 * Assumptions:
 * - The input data (e.g., CompanyStaff, Manager) is correctly populated and follows the expected structure.</li>
 * - The Manager class implements the getSubordinates() method to retrieve subordinate employees.</li>
 *
 * @author Neha B Acharya
 */
public class SalaryService {

    /**
     * Calculates the underpayment or overpayment for a manager based on the average subordinate salary.
     *
     * @param manager The manager for whom to calculate the payment.
     * @param multiplier The multiplier for expected salary calculation.
     * @return The absolute value of the underpayment or overpayment.
     */
    private BigDecimal calculateUnderOrOverPayment(Manager manager, BigDecimal multiplier) {
        BigDecimal averageSubordinateSalary = calculateAverageSubordinateSalary(manager);
        BigDecimal expectedSalary = averageSubordinateSalary.multiply(multiplier);
        BigDecimal managerSalary = manager.getSalary();
        BigDecimal underOrOverPayment = managerSalary.subtract(expectedSalary).abs();
        return underOrOverPayment.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Retrieves a map of underpaid managers along with their corresponding underpayment amounts.
     *
     * @param employees The map of all employees.
     * @return A map containing underpaid managers and their underpayment amounts.
     */
    public Map<Manager, BigDecimal> getUnderpaidManagers(Map<String, CompanyStaff> employees) {
        return getManagersBySalaryCondition(employees,
                BigDecimal.valueOf(1.2),
                (managerSalary, expectedSalary) -> managerSalary.compareTo(expectedSalary) < 0,
                BigDecimal.valueOf(1.2));
    }

    /**
     * Retrieves a map of overpaid managers along with their corresponding overpayment amounts.
     *
     * @param employees The map of all employees.
     * @return A map containing overpaid managers and their overpayment amounts.
     */
    public Map<Manager, BigDecimal> getOverpaidManagers(Map<String, CompanyStaff> employees) {
        return getManagersBySalaryCondition(employees,
                BigDecimal.valueOf(1.5),
                (managerSalary, expectedSalary) -> expectedSalary.compareTo(managerSalary) < 0,
                BigDecimal.valueOf(1.5));
    }

    /**
     * Retrieves a map of managers based on a specified salary condition.
     *
     * @param employees The map of all employees.
     * @param multiplier The multiplier for expected salary calculation.
     * @param salaryComparator The salary comparison condition (e.g., underpaid or overpaid).
     * @param underOrOverPaymentMultiplier The multiplier for underpayment or overpayment calculation.
     * @return A map containing managers meeting the specified salary condition and their corresponding underpayment or overpayment amounts.
     */
    private Map<Manager, BigDecimal> getManagersBySalaryCondition(Map<String, CompanyStaff> employees,
                                                                  BigDecimal multiplier,
                                                                  BiPredicate<BigDecimal, BigDecimal> salaryComparator,
                                                                  BigDecimal underOrOverPaymentMultiplier) {
        return employees.values().stream()
                .filter(Manager.class::isInstance)
                .map(Manager.class::cast)
                .filter(manager -> isSalaryConditionMet(manager, multiplier, salaryComparator))
                .collect(Collectors.toMap(Function.identity(), manager -> calculateUnderOrOverPayment(manager, underOrOverPaymentMultiplier)));
    }

    /**
     * Checks whether the salary condition is met for a given manager.
     *
     * @param manager The manager to evaluate.
     * @param multiplier The multiplier for expected salary calculation.
     * @param salaryComparator The salary comparison condition (e.g., underpaid or overpaid).
     * @return true if the salary condition is met, otherwise false.
     */
    private boolean isSalaryConditionMet(Manager manager,
                                         BigDecimal multiplier,
                                         BiPredicate<BigDecimal, BigDecimal> salaryComparator) {
        BigDecimal averageSubordinateSalary = calculateAverageSubordinateSalary(manager);
        BigDecimal managerSalary = manager.getSalary();
        BigDecimal expectedSalary = averageSubordinateSalary.multiply(multiplier);
        return salaryComparator.test(managerSalary, expectedSalary);
    }

    /**
     * Calculates the average salary of subordinates for a manager.
     *
     * @param employee The manager whose subordinates' salaries to consider.
     * @return The average subordinate salary.
     * @throws IllegalArgumentException If the input employee is not a manager.
     */
    public BigDecimal calculateAverageSubordinateSalary(CompanyStaff employee) {
        if (!(employee instanceof Manager manager)) {
            throw new IllegalArgumentException("CompanyStaff " + employee.getId() + " is not a manager");
        }
        List<CompanyStaff> subordinates = manager.getSubordinates();
        if (subordinates.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalSalary = subordinates.stream()
                .map(CompanyStaff::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalSalary.divide(BigDecimal.valueOf(subordinates.size()), RoundingMode.HALF_UP);
    }
}
