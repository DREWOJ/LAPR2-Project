package app.ui.console;

import java.util.ArrayList;
import java.util.List;
import app.controller.App;
import app.controller.RegisterVaccineController;
import app.domain.model.VaccineType;
import app.exception.CanceledMenuException;
import app.ui.console.utils.Utils;

/**
 * Register Vaccine View
 * 
 * @author Carlos Lopes <1211277@isep.ipp.pt>
 */
public class RegisterVaccineUI extends RegisterUI<RegisterVaccineController> {
  private String vacTypeId = "";

  public RegisterVaccineUI() {
    super(new RegisterVaccineController(App.getInstance().getCompany()));
  }

  @Override
  public void run() {
    System.out.println("\nRegister Vaccine:");

    List<VaccineType> vacTypes = super.ctrl.getVacTypes(); // all available vaccine types

    if (vacTypes.isEmpty()) {
      Utils.readLineFromConsole("No vaccine type registered. Register a vaccine Type before registering a new Vaccine.\nPress enter to go back to the menu. ");
      return;
    }

    displayVacTypes(vacTypes);

    VaccineType vacType = selectVacType(vacTypes); // asks to select the vaccine type

    if (vacType == null) return;

    this.vacTypeId = vacType.getCode();

    insertData(); // asks to insert vaccine data and instantiates and validates a new vaccine

    // CREATE ADMIN PROCESS
    boolean confirmed = true;
    while (confirmed) {
      // CREATE ADMIN PROCESS UI
      System.out.println("\nRegister administration process:");
      // asks to insert admin proc data and instantiates a new admin proc
      int numberOfDoses = insertAdminProcData();// this method returns the number of doses of the new admin proc

      for (int i = 1; i <= numberOfDoses; i++) {
        System.out.println("\nRegister information of dose number: " + i);

        insertDoseInfoData();// asks to insert dose info data and instantiates a new dose info
      }
      try {
        confirmed = askCreateAdminProc(); // asks the user if he wants to add a new ap
      } catch (CanceledMenuException ex) {
        System.out.println("\nCanceled.\n");
        return;
      }
    }

    confirmed = confirmData(super.ctrl.stringifyData());// asks to confirm data

    if (confirmed) {
      super.ctrl.save();
      System.out.println("Vaccine successfully registered!");
    }
  }

  // DISPLAY ALL AVAILABLE VACCINE TYPES
  private void displayVacTypes(List<VaccineType> vacTypes) {
    Utils.showList(vacTypes, "\nSelect a Vaccine Type");
  }

  // RETURNS VACCINE TYPE ID SELECTED
  private VaccineType selectVacType(List<VaccineType> vacTypes) {
    int vacTypeId = Utils.selectsIndex(vacTypes);

    if (vacTypeId == -1) return null;

    return vacTypes.get(vacTypeId);
  }

  // ASKS TO INSERT THE VACCINE DATA AND CREATES A NEW VACCINE
  @Override
  public void insertData() {
    String designation = Utils.readLineFromConsole("Designation: ");
    String id = Utils.readLineFromConsole("Id: ");
    String brand = Utils.readLineFromConsole("Brand: ");

    super.ctrl.createVaccine(designation, id, brand, this.vacTypeId);

    super.ctrl.validateVaccine();
  }

  // ASKS THE USER IF HE WANTS TO ADD A NEW ADMIN PROC
  private boolean askCreateAdminProc() throws CanceledMenuException {
    List<String> options = new ArrayList<String>();
    options.add("Yes, add a new administration process");
    options.add("No, I'm done");

    int index = Utils.showAndSelectIndex(options, "\nWant to add another administration process?:  ");
    if (index == -1) throw new CanceledMenuException("Invalid option");

    return index == 0;
  }

  // ASKS TO INSERT THE ADMIN PROC DATA AND RETURN THE NUMBER OF DOSES
  public int insertAdminProcData() {
    int minAge = 1;
    int maxAge = 0;
    do {
      minAge = Utils.readNonNegativeIntegerFromConsole("Min age: ");
      maxAge = Utils.readPositiveIntegerFromConsole("Max age: ");
      if (minAge > maxAge) System.out.println("Min age must be less than max age.");
    } while (minAge > maxAge);

    int numberOfDoses = Utils.readPositiveIntegerFromConsole("Number of doses: ");

    super.ctrl.createAdminProc(minAge, maxAge, numberOfDoses);

    super.ctrl.saveAdminProc();

    return numberOfDoses;
  }

  // ASKS TO INSERT THE DOSE INFO DATA
  public void insertDoseInfoData() {
    int dosage = Utils.readPositiveIntegerFromConsole("Dosage (ml): ");
    int timeSinceLastDose = Utils.readNonNegativeIntegerFromConsole("Time since last dose (days): ");

    super.ctrl.createDoseInfo(dosage, timeSinceLastDose);

    super.ctrl.saveDoseInfo();
  }
}
