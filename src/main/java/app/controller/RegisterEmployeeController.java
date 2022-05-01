package app.controller;

import app.domain.model.Company;
import app.domain.model.Employee;
import app.domain.model.store.EmployeeStore;

/**
 * Register SNS User Controller
 * 
 * @author Tomás Russo <1211288@isep.ipp.pt>
 */

public class RegisterEmployeeController {
    private App app;
    private Company company;
    private Employee employee;
    private EmployeeStore store;

    /**
     * Constructor for RegisterEmployeeController.
     */
    public RegisterEmployeeController() {
        this.app = App.getInstance();
        this.company = this.app.getCompany();
        this.store = this.company.getEmployeeStore();
        this.employee = null;
    }

    /**
     * Creates an Employee instance and validates it.
     * 
     * @param name the employee name
     * @param address the employee address
     * @param phoneNumber the employee phoneNumber
     * @param email the employee email
     * @param citizenCardNumber the employee citizenCardNumber
     * @param roleId the employee roleId
     */
    public void createEmployee(String name, String address, String phoneNumber, String email, int citizenCardNumber, String roleId) {
        // create an instance of an Employee
        this.employee = store.createEmployee(name, address, phoneNumber, email, citizenCardNumber, roleId);

        // validate the Employee
        store.validateEmployee(employee);
    }

    /**
     * Adds an Employee to the Employee store.
     */
    public void saveEmployee() {
        store.saveEmployee(this.employee);
    }  

    /**
     * Returns all Employee roles.
     * 
     * @return String 
     */
    public String getEmployeeRoles() {
        // TODO: implement this method
        return null;
    }
}