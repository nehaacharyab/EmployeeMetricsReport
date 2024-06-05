package org.bigcompany.service;

import org.bigcompany.model.CompanyStaff;

import java.util.Map;

/**
 * The IEmployeeService interface provides methods for managing employees within the company.
 *
 * @author Neha B Acharya
 */
public interface IEmployeeService {

    /**
     * Loads all employees from a data source.
     * The data source and loading mechanism are implementation-dependent.
     *
     * @return A map containing all employees, keyed by their unique identifiers.
     */
    Map<String, CompanyStaff> loadAllEmployee();
}

