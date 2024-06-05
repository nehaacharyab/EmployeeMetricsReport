package org.bigcompany.service;

import org.bigcompany.model.CompanyStaff;
import org.bigcompany.model.Manager;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The IReportingService interface provides methods for generating reports within the company.
 *
 * @author Neha B Acharya
 */
public interface IReportingService {

    /**
     * Generates a report of employees with a long reporting line, overpaid managers, and underpaid managers.
     * The report is printed to the console.
     */
    void generateEmployeeReport();

    /**
     * Retrieves a map of employees along with their reporting line lengths.
     * The reporting line length is defined as the number of managers above the employee in the hierarchy.
     *
     * @return A map containing employees and their reporting line lengths.
     */
    Map<CompanyStaff, Integer> getEmployeeReportingLineLengths();

    /**
     * Prints a report for a given set of managers and their overpayment or underpayment amounts to the console.
     *
     * @param title    the title of the report
     * @param managers a map of managers to their overpayment or underpayment amounts
     */
    void printPaymentReport(String title, Map<Manager, BigDecimal> managers);
}
