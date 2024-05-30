# Big Company Model

This project contains the model classes for a company's staff management system. It is written in Java and uses Maven for dependency management.

## Project Structure

The project follows a standard Maven project structure. The main code is located in the `src/main/java` directory.

The model classes are located in the `org.bigcompany.model` package and include:

- `Employee.java`: Represents an employee in the company.
- `Manager.java`: Represents a manager in the company. A manager is also an employee, but has additional properties and methods.
- `CompanyStaff.java`: An interface that both `Employee` and `Manager` classes implement.

## Getting Started

To build the project, navigate to the project root directory and run:

```bash
mvn clean install
```
## Running the Application

After building the project, you can run the application using the following command:

```bash
java -cp target/classes org.bigcompany.ReportGenerator
```