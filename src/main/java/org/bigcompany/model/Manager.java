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
public final class Manager  extends Employee {

    private final List<CompanyStaff> subordinates;

    /**
     * Constructs a Manager with the given parameters.
     *
     * @param id        the unique identifier of the manager
     * @param firstName the first name of the manager
     * @param lastName  the last name of the manager
     * @param salary    the salary of the manager
     * @param managerId the unique identifier of the manager's manager
     * @param subordinates the list of subordinates of the manager
     */
    public Manager(String id, String firstName, String lastName, BigDecimal salary, String managerId, List<CompanyStaff> subordinates) {
        super(id, firstName, lastName, salary, managerId);
        this.subordinates = Collections.unmodifiableList(new ArrayList<>(subordinates));
    }

    /**
     * Constructs a Manager with the given parameters.
     *
     * @param subordinates the list of subordinates of the manager
     */
    public Manager withSubordinates(List<CompanyStaff> subordinates) {
        return new Manager(this.getId(), this.getFirstName(), this.getLastName(), this.getSalary(), this.getManagerId(), subordinates);
    }

    /**
     * Returns the manager's list of subordinates.
     *
     * @return the manager's list of subordinates
 */
    public List<CompanyStaff> getSubordinates() {
        return Collections.unmodifiableList(subordinates);
    }

    /**
     * Returns the string representation of the Manager.
     *
     * @return the string representation of the Manager
     */
    @Override
    public String toString() {
        return "Manager{" +
                "id='" + getId() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", salary=" + getSalary() +
                ", managerId='" + getManagerId() + '\'' +
                ", subordinates=" + subordinates +
                '}';
    }

    /**
     * Returns the reporting line length of the Manager.
     *
     * @return the reporting line length of the Manager
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return getId().equals(manager.getId()) &&
                getFirstName().equals(manager.getFirstName()) &&
                getLastName().equals(manager.getLastName()) &&
                getSalary().equals(manager.getSalary()) &&
                Objects.equals(getManagerId(), manager.getManagerId()) &&
                subordinates.equals(manager.subordinates);
    }

    /**
     * Returns the hash code of the Manager.
     *
     * @return the hash code of the Manager
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getSalary(), getManagerId(), subordinates);
    }

}