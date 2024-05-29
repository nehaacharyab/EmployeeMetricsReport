package org.bigcompany.service;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SalaryService {


    private BigDecimal calculateUnderOrOverPayment(Manager manager, BigDecimal multiplier) {
        BigDecimal averageSubordinateSalary = calculateAverageSubordinateSalary(manager);
        BigDecimal expectedSalary = averageSubordinateSalary.multiply(multiplier);
        BigDecimal managerSalary = manager.getSalary();
        BigDecimal underOrOverPayment = managerSalary.subtract(expectedSalary);
        return underOrOverPayment.abs();
    }

    public Map<Manager, BigDecimal> getUnderpaidManagers(Map<String, CompanyStaff> employees) {
        return getManagersBySalaryCondition(employees,
                BigDecimal.valueOf(1.2),
                (managerSalary, expectedSalary) -> managerSalary.compareTo(expectedSalary) < 0,
                BigDecimal.valueOf(1.2));
    }

    public Map<Manager, BigDecimal> getOverpaidManagers(Map<String, CompanyStaff> employees) {
        return getManagersBySalaryCondition(employees,
                BigDecimal.valueOf(1.5),
                (managerSalary, expectedSalary) -> expectedSalary.compareTo(managerSalary) < 0,
                BigDecimal.valueOf(1.5));
    }

    private Map<Manager, BigDecimal> getManagersBySalaryCondition(Map<String, CompanyStaff> employees, BigDecimal multiplier, BiPredicate<BigDecimal, BigDecimal> salaryComparator, BigDecimal underOrOverPaymentMultiplier) {
        return employees.values().stream()
                .filter(Manager.class::isInstance)
                .map(Manager.class::cast)
                .filter(manager -> isSalaryConditionMet(manager, multiplier, salaryComparator))
                .collect(Collectors.toMap(Function.identity(), manager -> calculateUnderOrOverPayment(manager, underOrOverPaymentMultiplier)));
    }

    private boolean isSalaryConditionMet(Manager manager, BigDecimal multiplier, BiPredicate<BigDecimal, BigDecimal> salaryComparator) {
        BigDecimal averageSubordinateSalary = calculateAverageSubordinateSalary(manager);
        BigDecimal managerSalary = manager.getSalary();
        BigDecimal expectedSalary = averageSubordinateSalary.multiply(multiplier);
        return salaryComparator.test(managerSalary, expectedSalary);
    }

    public BigDecimal calculateAverageSubordinateSalary(CompanyStaff employee) {
        if (!(employee instanceof Manager manager)) {
            throw new IllegalArgumentException("CompanyStaff " + employee.getId() + " is not a manager");
        }
        return manager.getSubordinates().stream()
                .map(CompanyStaff::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(manager.getSubordinates().size()), RoundingMode.HALF_UP);
    }

}
