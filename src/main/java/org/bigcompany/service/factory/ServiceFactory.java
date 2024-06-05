package org.bigcompany.service.factory;

import org.bigcompany.dao.impl.EmployeeCSVLoader;
import org.bigcompany.service.IEmployeeService;
import org.bigcompany.service.ISalaryService;
import org.bigcompany.service.impl.EmployeeService;
import org.bigcompany.service.impl.SalaryService;

public class ServiceFactory {

    private static IEmployeeService employeeService;
    private static ISalaryService salaryService;

    public static IEmployeeService createEmployeeService() {
        if (employeeService == null) {
            employeeService = new EmployeeService(new EmployeeCSVLoader());
        }
        return employeeService;
    }

    public static ISalaryService createSalaryService() {
        if (salaryService == null) {
            salaryService = new SalaryService();
        }
        return salaryService;
    }

    public static void setEmployeeService(IEmployeeService employeeService) {
        ServiceFactory.employeeService = employeeService;
    }

    public static void setSalaryService(ISalaryService salaryService) {
        ServiceFactory.salaryService = salaryService;
    }
}