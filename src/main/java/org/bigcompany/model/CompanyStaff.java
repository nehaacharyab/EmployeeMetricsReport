package org.bigcompany.model;

import java.math.BigDecimal;

/**
 * This interface represents a staff member in the company.
 * It provides methods to get and set the properties of a staff member.
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

    /**
     * Returns the reporting line length of the staff member.
     *
     * @return the reporting line length of the staff member
     */
    String getReportingLineLength();

    /**
     * Sets the reporting line length of the staff member.
     *
     * @param reportingLineLength the reporting line length to set
     */
    void setReportingLineLength(String reportingLineLength);
}