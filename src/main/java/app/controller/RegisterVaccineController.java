package app.controller;

import java.util.List;
import app.domain.model.AdminProcess;
import app.domain.model.Company;
import app.domain.model.DoseInfo;
import app.domain.model.Vaccine;
import app.domain.model.VaccineType;
import app.domain.model.store.VaccineStore;
import app.domain.model.store.VaccineTypeStore;


/**
 * Create Vaccine Controller
 * 
 * @author Carlos Lopes <1211277@isep.ipp.pt>
 */
public class RegisterVaccineController implements IRegisterController<Vaccine> {
  private Company company;
  private VaccineStore vaccineStore;
  private VaccineTypeStore vaccineTypeStore;
  private Vaccine vac;
  private AdminProcess ap;
  private DoseInfo di;

  /**
   * Constructor for CreateVaccineController
   */
  public RegisterVaccineController(Company company) {
    this.company = company;
    this.vaccineStore = this.company.getVaccineStore();
    this.vaccineTypeStore = this.company.getVaccineTypeStore();
    this.vac = null;
    this.ap = null;
    this.di = null;
  }

  // GET ALL AVAILABLE VACCINE TYPES
  public List<VaccineType> getVacTypes() {
    return vaccineTypeStore.getList();
  }


  // CREATE VACCINE
  public void createVaccine(String designation, String id, String brand, String vacTypeId) {
    VaccineType vacType = vaccineTypeStore.getVaccineTypeByCode(vacTypeId);
    this.vac = vaccineStore.createVaccine(designation, id, brand, vacType);
  }

  // SAVE VACCINE
  @Override
  public void save() {
    vaccineStore.saveVaccine(vac);
  }

  // CREATE ADMINISTRATION PROCESS
  public void createAdminProc(int minAge, int maxAge, int numberOfDoses) {
    ap = vac.createAdminProc(minAge, maxAge, numberOfDoses);
  }

  // SAVE ADMINISTRATION PROCESS
  public void saveAdminProc() {
    vac.addAdminProc(ap);
  }

  // CREATE DOSE INFO
  public void createDoseInfo(int dosage, int timeSinceLastDose) {
    di = ap.createDoseInfo(dosage, timeSinceLastDose);
  }

  // SAVE DOSE INFO
  public void saveDoseInfo() {
    ap.addDoseInfo(di);
  }

  // VALIDATE VACCINE
  public boolean validateVaccine() {
    return (vaccineStore.exists(this.vac.getDesignation()) ? true : false);
  }

  // RETURN A STRING WITH ALL VACCINE DATA
  @Override
  public String stringifyData() {
    String result = vac.toString();
    List<AdminProcess> adminProcList = vac.getAdminProcList().getList();
    for (int i = 0; i < adminProcList.size(); i++) {
      result += "\n\nAdministration process number: " + (i + 1) + "\n" + adminProcList.get(i).toString();
      List<DoseInfo> doseInfoList = adminProcList.get(i).getDoseInfoList().getList();
      for (int j = 0; j < doseInfoList.size(); j++) {
        result += "\n\nInformation of dose number: " + (j + 1) + "\n" + doseInfoList.get(j).toString();
      }
    }

    return result;
  }

  @Override
  public String getResourceName() {
    return "Vaccine";
  }

  @Override
  public Vaccine getRegisteredObject() {
    return vac;
  }
}
