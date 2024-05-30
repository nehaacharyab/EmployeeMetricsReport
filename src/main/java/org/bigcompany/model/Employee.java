package org.bigcompany.model;

import java.math.BigDecimal;

/**
 * Represents an Employee in the company.
 * Employee is a regular Company staff and will not have any subordinates
 *
 * @author Neha B Acharya
 */
public class Employee implements CompanyStaff {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final String managerId;
    private String reportingLineLength;

    /**
     * Constructs an Employee with the given parameters.
     *
     * @param id        the unique identifier of the employee
     * @param firstName the first name of the employee
     * @param lastName  the last name of the employee
     * @param salary    the salary of the employee
     * @param managerId the unique identifier of the employee's manager
     */
    public Employee(String id, String firstName, String lastName, BigDecimal salary, String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    /**
     * Returns a string representation of the Employee.
     *
     * @return a string representation of the Employee
     */
    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId='" + managerId + '\'' +
                ", reportingLineLength='" + reportingLineLength + '\'' +
                '}';
    }

    /**
     * Returns the unique identifier of the Employee.
     *
     * @return the unique identifier of the Employee
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the first name of the Employee.
     *
     * @return the first name of the Employee
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the Employee.
     *
     * @return the last name of the Employee
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the salary of the Employee.
     *
     * @return the salary of the Employee
     */
    @Override
    public BigDecimal getSalary() {
        return salary;
    }

    /**
     * Returns the unique identifier of the Employee's manager.
     *
     * @return the unique identifier of the Employee's manager
     */
    @Override
    public String getManagerId() {
        return managerId;
    }

    /**
     * Returns the reporting line length of the Employee.
     *
     * @return the reporting line length of the Employee
     */
    @Override
    public String getReportingLineLength() {
        return reportingLineLength;
    }

    /**
     * Sets the reporting line length of the Employee.
     *
     * @param reportingLineLength the reporting line length to set
     */
    @Override
    public void setReportingLineLength(String reportingLineLength) {
        this.reportingLineLength = reportingLineLength;
    }

}