package org.bigcompany.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * The Manager class represents a manager in the company.
 * It implements the CompanyStaff interface and provides methods to get the manager's ID, first name, last name, salary, manager's ID, and subordinates.
 *
 * @author Neha B Acharya
 */
public final class Manager implements CompanyStaff {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final String managerId;
    private final List<CompanyStaff> subordinates;


    /**
     * Constructs a Manager with the given ID, first name, last name, salary, manager's ID, and subordinates.
     *
     * @param id the unique identifier of the manager
     * @param firstName the first name of the manager
     * @param lastName the last name of the manager
     * @param salary the salary of the manager
     * @param managerId the unique identifier of the manager's manager
     * @param subordinates the list of subordinates of the manager
     */
    public Manager(String id, String firstName, String lastName, BigDecimal salary, String managerId, List<CompanyStaff> subordinates) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
        this.subordinates = Collections.unmodifiableList(new ArrayList<>(subordinates));
    }

    /**
     * Returns a Manager with the given subordinates.
     * @param subordinates the list of subordinates of the manager
     * @return a Manager with the given subordinates
     */
    public Manager withSubordinates(List<CompanyStaff> subordinates) {
        return new Manager(this.getId(), this.getFirstName(), this.getLastName(), this.getSalary(), this.getManagerId(), subordinates);
    }


    /**
     * Returns the list of subordinates of the manager.
     * @return the list of subordinates of the manager
     */
    public List<CompanyStaff> getSubordinates() {
        return Collections.unmodifiableList(subordinates);
    }


    /**
     * Returns the unique identifier of the manager.
     * @return the unique identifier of the manager
     */
    @Override
    public String getId() {
        return id;
    }


    /**
     * Returns the first name of the manager.
     * @return the first name of the manager
     */
    @Override
    public String getFirstName() {
        return firstName;
    }


    /**
     * Returns the last name of the manager.
     * @return the last name of the manager
     */
    @Override
    public String getLastName() {
        return lastName;
    }


    /**
     * Returns the salary of the manager.
     * @return the salary of the manager
     */
    @Override
    public BigDecimal getSalary() {
        return salary;
    }


    /**
     * Returns the unique identifier of the manager's manager.
     * @return the unique identifier of the manager's manager
     */
    @Override
    public String getManagerId() {
        return managerId;
    }

    /**
     * Returns a string representation of the manager.
     * @return a string representation of the manager
     */
    @Override
    public String toString() {
        return "Manager{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId='" + managerId + '\'' +
                ", subordinates=" + subordinates +
                '}';
    }

    /**
     * Checks if the given object is equal to this manager.
     * @param o the object to compare this Manager against
     * @return true if the given object represents a Manager equivalent to this manager, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return id.equals(manager.id) &&
               firstName.equals(manager.firstName) &&
               lastName.equals(manager.lastName) &&
               salary.equals(manager.salary) &&
               Objects.equals(managerId, manager.managerId) &&
               subordinates.equals(manager.subordinates);
    }

    /**
     * Returns a hash code value for the manager.
     * @return a hash code value for this manager
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, salary, managerId, subordinates);
    }

}