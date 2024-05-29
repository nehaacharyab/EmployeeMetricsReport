package org.bigcompany;

import org.bigcompany.dao.EmployeeDAO;
import org.bigcompany.service.EmployeeService;
import org.bigcompany.service.SalaryService;

public class ReportGenerator {

    public static void main(String[] args) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        SalaryService salaryService = new SalaryService();
        EmployeeService employeeService = new EmployeeService(employeeDAO, salaryService);
        employeeService.generateEmployeeReport();
    }
}
