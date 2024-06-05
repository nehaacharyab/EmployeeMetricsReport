package org.bigcompany.service;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The ISalaryService interface provides methods for salary-related calculations within the company.
 * It provides methods to identify underpaid and overpaid managers.
 *
 * @author Neha B Acharya
 */
public interface ISalaryService {

    /**
     * Retrieves a map of overpaid managers along with their corresponding overpayment amounts.
     *
     * @param employees The map of all employees.
     * @return A map containing overpaid managers and their overpayment amounts.
     */
    Map<Manager, BigDecimal> getOverpaidManagers(Map<String, CompanyStaff> employees);

    /**
     * Retrieves a map of underpaid managers along with their corresponding underpayment amounts.
     *
     * @param employees The map of all employees.
     * @return A map containing underpaid managers and their underpayment amounts.
     */
    Map<Manager, BigDecimal> getUnderpaidManagers(Map<String, CompanyStaff> employees);
}
