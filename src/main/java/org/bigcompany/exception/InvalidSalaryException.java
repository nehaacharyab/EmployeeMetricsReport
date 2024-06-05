package org.bigcompany.exception;
/**
 * The InvalidSalaryException class is a custom exception that is thrown when an invalid salary is encountered.
 * An invalid salary is one that is negative or zero.
 * It extends the RuntimeException class.
 *
 * @author Neha B Acharya
 */
public class InvalidSalaryException extends RuntimeException {
    public InvalidSalaryException(String message) {
        super(message);
    }

    public InvalidSalaryException(String message, Throwable cause) {
        super(message, cause);
    }
}