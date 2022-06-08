package app.controller;

import app.domain.model.Company;
import app.domain.model.VaccinationCenter;
import app.domain.model.store.VaccinationCenterStore;
import app.session.EmployeeSession;

public class FindCoordinatorVaccinationCenterController {
  private VaccinationCenterStore vaccinationCenterStore;
  private EmployeeSession session;

  public FindCoordinatorVaccinationCenterController(Company company, EmployeeSession session) {
    this.vaccinationCenterStore = company.getVaccinationCenterStore();
    this.session = session;
  }

  public void findCoordinatorCenter() {
    String email = App.getInstance().getCurrentUserSession().getUserId().getEmail();
    VaccinationCenter center = this.vaccinationCenterStore.getVaccinationCenterWithCoordinatorEmail(email);
    this.session.setVaccinationCenter(center);
  }

  public String getVaccinationCenterName() {
    if (!this.session.hasCenter()) return null;
    return this.session.getVaccinationCenter().getName();
  }
}
