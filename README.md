# Big Company Model

This project contains the model classes for a company's staff management system. It is written in Java and uses Maven for dependency management.

## Project Structure

The project follows a standard Maven project structure. The main code is located in the `src/main/java` directory.

The model classes are located in the `org.bigcompany.model` package and include:

- `Employee.java`: Represents an employee in the company.
- `Manager.java`: Represents a manager in the company. A manager is also an employee, but has additional properties and methods.
- `CompanyStaff.java`: An interface that both `Employee` and `Manager` classes implement.

## Assumptions

- The first line of the CSV file is a header and should be skipped.
- The salary field can be parsed as a BigDecimal and should be greater than zero
- The CSV file from which employees are loaded is always correctly formatted and located at the specified path
  and follow the expected structure, if not exception will be encountered.
- Calculation of reporting line between employee and CEO is done excluding both employee and CEO
- The CSV file tagged to this project is big_company_1000_records.csv and contains 1000 records in total 


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