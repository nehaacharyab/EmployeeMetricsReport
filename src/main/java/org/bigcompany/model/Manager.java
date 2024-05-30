package org.bigcompany.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Manager in the company.
 */
public class Manager implements CompanyStaff {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final String managerId;
    private String reportingLine;
    private final List<CompanyStaff> subordinates = new ArrayList<>();

    /**
     * Constructs a Manager with the given parameters.
     *
     * @param id the unique identifier of the manager
     * @param firstName the first name of the manager
     * @param lastName the last name of the manager
     * @param salary the salary of the manager
     * @param managerId the unique identifier of the manager's manager
     */
    public Manager(String id, String firstName, String lastName, BigDecimal salary, String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    /**
     * Adds a subordinate to the manager's list of subordinates.
     *
     * @param e the subordinate to add
     */
    public void addSubordinate(CompanyStaff e) {
        subordinates.add(e);
    }

    /**
     * Returns the manager's list of subordinates.
     *
     * @return the manager's list of subordinates
     */
    public List<CompanyStaff> getSubordinates() {
        return subordinates;
    }

    /**
     * Returns the unique identifier of the Manager.
     *
     * @return the unique identifier of the Manager
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Returns the first name of the Manager.
     *
     * @return the first name of the Manager
     */
    @Override
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Returns the last name of the Manager.
     *
     * @return the last name of the Manager
     */
    @Override
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Returns the salary of the Manager.
     *
     * @return the salary of the Manager
     */
    @Override
    public BigDecimal getSalary() {
        return this.salary;
    }

    /**
     * Returns the unique identifier of the Manager's manager.
     *
     * @return the unique identifier of the Manager's manager
     */
    @Override
    public String getManagerId() {
        return this.managerId;
    }

    /**
     * Returns the reporting line length of the Manager.
     *
     * @return the reporting line length of the Manager
     */
    @Override
    public String getReportingLineLength() {
        return this.reportingLine;
    }

    /**
     * Sets the reporting line length of the Manager.
     *
     * @param reportingLineLength the reporting line length to set
     */
    @Override
    public void setReportingLineLength(String reportingLineLength) {
        this.reportingLine = reportingLineLength;
    }

    /**
     * Returns a string representation of the Manager.
     *
     * @return a string representation of the Manager
     */
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId=" + managerId +
                ", subordinates=" + subordinates +
                '}';
    }

}