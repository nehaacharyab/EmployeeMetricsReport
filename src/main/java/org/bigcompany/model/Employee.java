package org.bigcompany.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The Employee class represents an employee in the company.
 * It implements the CompanyStaff interface and provides methods to get the employee's ID, first name, last name, salary, and manager's ID.
 *
 * @author Neha B Acharya
 */
public class Employee implements CompanyStaff {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final String managerId;

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
     * Returns the string representation of the Employee.
     *
     * @return the string representation of the Employee
     */
    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId='" + managerId + '\'' +
                '}';
    }

    /**
     * Returns the hash code of the Employee.
     *
     * @return the hash code of the Employee
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id) &&
                firstName.equals(employee.firstName) &&
                lastName.equals(employee.lastName) &&
                salary.equals(employee.salary) &&
                Objects.equals(managerId, employee.managerId);
    }

    /**
     * Returns the hash code of the Employee.
     *
     * @return the hash code of the Employee
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, salary, managerId);
    }
}