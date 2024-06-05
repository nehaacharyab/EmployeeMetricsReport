package org.bigcompany.model;

import java.math.BigDecimal;

/**
 * The CompanyStaff interface represents a staff member in the company.
 * It provides methods to get the staff member's ID, first name, last name, salary, and manager's ID.
 *
 * @author Neha B Acharya
 */
public interface CompanyStaff {

    /**
     * Returns the unique identifier of the staff member.
     *
     * @return the unique identifier of the staff member
     */
    String getId();

    /**
     * Returns the first name of the staff member.
     *
     * @return the first name of the staff member
     */
    String getFirstName();

    /**
     * Returns the last name of the staff member.
     *
     * @return the last name of the staff member
     */
    String getLastName();

    /**
     * Returns the salary of the staff member.
     *
     * @return the salary of the staff member
     */
    BigDecimal getSalary();

    /**
     * Returns the unique identifier of the staff member's manager.
     *
     * @return the unique identifier of the staff member's manager
     */
    String getManagerId();
}