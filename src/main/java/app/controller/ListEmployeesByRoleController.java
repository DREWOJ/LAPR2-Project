package app.controller;

import java.util.List;
import app.domain.model.Company;
import app.domain.model.Employee;
import app.domain.model.MyUserRole;
import app.domain.model.store.EmployeeRoleStore;
import app.domain.model.store.EmployeeStore;

/**
 * List Employees by Role Controller
 * 
 * @author Tomás Lopes <1211289@isep.ipp.pt>
 */
public class ListEmployeesByRoleController {
  private Company company;
  private EmployeeStore employeeStore;
  private EmployeeRoleStore rolesStore;

  /**
   * Constructor for ListEmployeesByRole
   */
  public ListEmployeesByRoleController(Company company) {
    this.company = company;
    this.employeeStore = this.company.getEmployeeStore();
    this.rolesStore = this.company.getEmployeeRoleStore();
  }

  /**
   * Lists all the employees for a given role
   * 
   * @param role
   * @return Employee List with Roles
   */
  public List<Employee> getEmployeesWithRole(String roleId) {
    return employeeStore.getEmployeesWithRole(roleId);
  }

  /**
   * @return all the existing employee roles
   */
  public List<MyUserRole> getEmployeeRoles() {
    return rolesStore.getRoles();
  }
}
