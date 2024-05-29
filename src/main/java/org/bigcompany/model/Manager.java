package org.bigcompany.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Manager implements CompanyStaff {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final String managerId;
    private String reportingLine;
    private final List<CompanyStaff> subordinates = new ArrayList<>();

    private Manager(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.salary = builder.salary;
        this.managerId = builder.managerId;
        this.reportingLine = builder.reportingLine;
    }

    public void addSubordinate(CompanyStaff e) {
        subordinates.add(e);
    }

    public List<CompanyStaff> getSubordinates() {
        return subordinates;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public BigDecimal getSalary() {
        return this.salary;
    }

    @Override
    public String getManagerId() {
        return this.managerId;
    }

    @Override
    public String getReportingLineLength() {
        return this.reportingLine;
    }

    @Override
    public void setReportingLineLength(String reportingLineLength) {
        this.reportingLine = reportingLineLength;
    }

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

    public static class Builder {
        private String id;
        private String firstName;
        private String lastName;
        private BigDecimal salary;
        private String managerId;
        private String reportingLine;
        private List<CompanyStaff> subordinates;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setSalary(BigDecimal salary) {
            this.salary = salary;
            return this;
        }

        public Builder setManagerId(String managerId) {
            this.managerId = managerId;
            return this;
        }

        public Builder setReportingLineLength(String reportingLineLength) {
            this.reportingLine = reportingLineLength;
            return this;
        }

        public Builder setSubordinates(List<CompanyStaff> subordinates) {
            this.subordinates = subordinates;
            return this;
        }

        public Manager build() {
            return new Manager(this);
        }
    }
}
