package app.controller;

import app.domain.model.Company;
import app.domain.model.VaccineType;
import app.domain.model.store.VaccineTypeStore;

/**
 * Specify New Vaccine Type Controller
 * 
 * @author Tomás Lopes <1211289@isep.ipp.pt>
 */

public class RegisterNewVaccineTypeController implements IRegisterController {
  private App app;
  private Company company;
  private VaccineTypeStore vaccineTypeStore;
  private VaccineType vt;

  /**
   * Constructor for SpecifyNewVaccineTypeController
   */

  public RegisterNewVaccineTypeController() {
    this.app = App.getInstance();
    this.company = this.app.getCompany();
    this.vaccineTypeStore = this.company.getVaccineTypeStore();
  }

  /**
   * Method to add a new vaccine type to vaccine type list
   * 
   * @param code the vaccine type code
   * @param description the vaccine type description
   * @param technology the vaccine type technology
   * @return vaccine type
   */

  public VaccineType create(String code, String description, String technology) {
    this.vt = vaccineTypeStore.addVaccineType(code, description, technology);

    return this.vt;
  }

  @Override
  public void save() {
    vaccineTypeStore.saveVaccineType(this.vt);
  }

  @Override
  public String stringifyData() {
    return this.vt.toString();
  }

  @Override
  public String getResourceName() {
    return "Vaccine Type";
  }
}
