package org.bigcompany.model;

import java.math.BigDecimal;
import java.util.Objects;



/**
 * The Employee class represents an employee in the company.
 * It implements the CompanyStaff interface and provides methods to get the employee's ID, first name, last name, salary, and manager's ID.
 *
 * @author Neha B Acharya

 */
public record Employee(String getId, String getFirstName, String getLastName, BigDecimal getSalary, String getManagerId)
        implements CompanyStaff {


    /**
     * Returns a string representation of the employee.
     *
     * @return a string representation of the employee
     */
    @Override
    public String toString() {
        return "Employee{" +
               "id='" + getId + '\'' +
               ", firstName='" + getFirstName + '\'' +
               ", lastName='" + getLastName + '\'' +
               ", salary=" + getSalary +
               ", managerId='" + getManagerId + '\'' +
               '}';
    }


    /**
     * Checks if the given object is equal to this employee.
     * The result is true if and only if the argument is not null and is an Employee object that has the same ID, first name, last name, salary, and manager ID as this object.
     *
     * @param o the object to compare this Employee against
     * @return true if the given object represents an Employee equivalent to this employee, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getId.equals(employee.getId) &&
               getFirstName.equals(employee.getFirstName) &&
               getLastName.equals(employee.getLastName) &&
               getSalary.equals(employee.getSalary) &&
               Objects.equals(getManagerId, employee.getManagerId);
    }

    /**
     * Returns a hash code value for the employee.
     * This method is supported for the benefit of hash tables such as those provided by HashMap.
     * The hash code is calculated based on the employee's ID, first name, last name, salary, and manager ID.
     *
     * @return a hash code value for this employee
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId, getFirstName, getLastName, getSalary, getManagerId);
    }

}