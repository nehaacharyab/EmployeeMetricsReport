# Big Company Model

This project is a staff metrics analysis system for a large company. 
It is written in Java and uses Maven for dependency management. 
The system generates a report of employees 
- who have a reporting line which is too long and the reporting line length
- Overpaid managers and the overpayment amount
- Underpaid managers and the underpaid amount

The outcome of the report is printed to the console.


## Project Structure

The project follows a standard Maven project structure. The main code is located in the src/main/java directory.

The project is divided into several packages:

- org.bigcompany.model: Contains the model classes, including Employee.java, Manager.java, and CompanyStaff.java.
- org.bigcompany.service: Contains service classes like EmployeeService.java that contain the business logic of the application.
- org.bigcompany.dao: Contains data access object classes like EmployeeCSVLoader.java for reading employee data from CSV files.

## Assumptions

- The first line of the CSV file is a header and should be skipped.
- The salary field can be parsed as a BigDecimal and should be greater than zero
- The CSV file from which employees are loaded is always correctly formatted and located at the specified path
  and follow the expected structure, if not exception will be encountered.
- Calculation of reporting line between employee and CEO is done excluding both employee and CEO
- The CSV file tagged to this project is big_company_1000_records.csv and contains 1000 records in total 

## Dependencies

The project has the following dependencies:

- Java: version 22.0.1
- Maven: version 3.6.3

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