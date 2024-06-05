package org.bigcompany.dao;

import org.bigcompany.exception.EmployeeDataException;
import org.bigcompany.model.CompanyStaff;

import java.util.Map;

/**
 * The IEmployeeCSVLoader interface provides a method for loading employee data from a CSV file.
 * The specific implementation of how the data is loaded and parsed is left to the implementing class.
 *
 * @author Neha B Acharya

 */
public interface IEmployeeCSVLoader {

    /**
     * Reads a CSV file and builds a map of employees.
     * Each line in the CSV file represents an Employee.
     * The map's keys are Employee IDs, and the values are CompanyStaff objects.
     *
     * @param csvFilePath The path to the CSV file.
     * @return A map of employees, keyed by their unique identifiers.
     * @throws EmployeeDataException If there is an error reading employee data from the CSV file.
     */
    Map<String, CompanyStaff> buildEmployeeMapFromCSV(String csvFilePath) throws EmployeeDataException;
}
