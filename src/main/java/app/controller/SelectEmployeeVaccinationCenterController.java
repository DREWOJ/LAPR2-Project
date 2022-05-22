package app.controller;

import java.util.List;
import app.domain.model.Company;
import app.domain.model.dto.VaccinationCenterListDTO;
import app.domain.model.store.VaccinationCenterStore;
import app.session.EmployeeSession;

public class SelectEmployeeVaccinationCenterController {
  private VaccinationCenterStore vaccinationCenterStore;

  public SelectEmployeeVaccinationCenterController(Company company) {
    this.vaccinationCenterStore = company.getVaccinationCenterStore();
  }

  public List<VaccinationCenterListDTO> getVaccinationCentersList() {
    return vaccinationCenterStore.getVaccinationCenters();
  }

  public void selectVaccinationCenter(VaccinationCenterListDTO vaccinationCenter,
      EmployeeSession employeeSession) {
    if (vaccinationCenterStore.exists(vaccinationCenter.getPhone()))
      employeeSession.setVaccinationCenter(vaccinationCenter);
  }
}
