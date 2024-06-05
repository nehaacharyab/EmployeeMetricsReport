package org.bigcompany.exception;
/**
 * The EmployeeDataException class is a custom exception that is thrown when there is an error reading employee data from the CSV file.
 * This exception is used to indicate that there is an error reading employee data.
 * It extends the RuntimeException class.
 *
* @author Neha B Acharya
 */
public class EmployeeDataException extends RuntimeException {
    public EmployeeDataException(String message) {
        super(message);
    }
}
