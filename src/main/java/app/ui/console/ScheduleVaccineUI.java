package app.ui.console;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import app.controller.App;
import app.controller.ScheduleVaccineController;
import app.domain.model.VaccinationCenter;
import app.domain.model.VaccineType;
import app.domain.shared.FieldToValidate;
import app.dto.AppointmentWithoutNumberDTO;
import app.dto.VaccinationCenterListDTO;
import app.dto.VaccineTypeDTO;
import app.service.CalendarUtils;
import app.ui.console.utils.Utils;

public class ScheduleVaccineUI extends RegisterUI<ScheduleVaccineController> {

  public ScheduleVaccineUI() {
    super(new ScheduleVaccineController(App.getInstance().getCompany()));
  }

  public void insertData() {
    VaccineType vaccineType = ctrl.getSuggestedVaccineType();

    boolean accepted = showSuggestedVaccineType(vaccineType);
    boolean isEligible = isUserEligibleForVaccine(vaccineType);

    if (!accepted || !isEligible) {
      // Declines the suggested vaccine type or is not eligible for vaccine type selected

      if (!isEligible) {
        System.out.println(
            "\nYou are not eligible for any vaccine of this type. Please select other type.");
      }

      vaccineType = selectVaccineType();

      if (vaccineType == null) {
        return;
      }
    }

    VaccinationCenter vacCenter = null;

    if (checkIfUserHasTakenVaccineType(vaccineType)) {
      // ctrl.getVaccinesByType(vaccineType);
      // ctrl.checkAdministrationProcessForNextDose();
      // vacCenter = selectVaccinationCenterWithVaccineType(vaccineType);
    } else {
      if (ctrl.checkAdministrationProcessForVaccineType(vaccineType)) {
        vacCenter = selectVaccinationCenterWithVaccineType(vaccineType);
      } else {
        System.out.println("\nYou are not eligible for any vaccine of this type.\n");
        return;
      }
    }

    Calendar appointmentDate = selectDateAndTimeInCenterAvailability(vacCenter);

    boolean sms = selectSMS();

    AppointmentWithoutNumberDTO appointmentDto =
        new AppointmentWithoutNumberDTO(appointmentDate, vacCenter, vaccineType, sms);

    ctrl.createAppointment(appointmentDto);
  }

  private boolean showSuggestedVaccineType(VaccineType vt) {
    System.out.println("\nSuggested Vaccine Type:\n");

    System.out.println(vt.getDescription());

    List<String> options = new ArrayList<String>();
    options.add("Yes, accept suggestion.");
    options.add("No, choose other vaccine type.");
    int index = Utils.showAndSelectIndex(options, "\nSelect an option: (1 or 2)  ");

    return index == 0;
  }

  private VaccineType selectVaccineType() {
    List<VaccineTypeDTO> list = ctrl.getListOfVaccineTypes();

    boolean accepted;

    do {
      accepted = false;

      Object selectedVt = Utils.showAndSelectOne(list, "\n\nSelect a Vaccine Type:\n");
      VaccineType vaccineType;

      if (selectedVt == null) {
        return null;
      }

      try {
        VaccineTypeDTO vtDto = (VaccineTypeDTO) selectedVt;
        vaccineType = ctrl.getVaccineTypeByCode(vtDto.getCode());

        if (isUserEligibleForVaccine(vaccineType)) {
          accepted = true;
          return vaccineType;
        } else {
          System.out.println(
              "\nYou are not eligible for any vaccine of this type. Please select other type.");
          accepted = false;
        }
      } catch (ClassCastException e) {
        System.out.println("\n\nInvalid selection.");
        return null;
      }
    } while (!accepted);

    return null;
  }

  private VaccinationCenter selectVaccinationCenterWithVaccineType(VaccineType vt) {
    List<VaccinationCenterListDTO> list = ctrl.getListOfVaccinationCentersWithVaccineType(vt);

    Object selectedCenter = Utils.showAndSelectOne(list, "\nSelect a Vaccination Center:\n");

    if (selectedCenter == null) {
      return null;
    }

    try {
      VaccinationCenterListDTO centerDto = (VaccinationCenterListDTO) selectedCenter;
      return ctrl.getVaccinationCenterByEmail(centerDto.getEmail());
    } catch (ClassCastException e) {
      System.out.println("\n\nInvalid selection.");
      return null;
    }
  }

  private boolean selectSMS() {
    System.out.println("\nDo you want to receive an SMS with the appointment's info?");
    List<String> options = new ArrayList<String>();
    options.add("Yes, send me an SMS.");
    options.add("No, don't send me an SMS.");
    int index = Utils.showAndSelectIndex(options, "\nSelect an option: (1 or 2)  ");

    return (index == 0);
  }

  private boolean checkIfUserHasTakenVaccineType(VaccineType vt) {
    return ctrl.checkIfUserHasTakenVaccineType(vt);
  }

  private Calendar selectDateAndTimeInCenterAvailability(VaccinationCenter center) {
    boolean accepted = true;
    Date date = new Date();
    String hours;

    do {
      accepted = true;

      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      String dateStr =
          Utils.readLineFromConsoleWithValidation("Date (dd/MM/yyyy): ", FieldToValidate.DATE);
      try {
        date = df.parse(dateStr);
      } catch (ParseException ex) {
        System.out.println("Invalid date format.\n");
      }
      hours = Utils.readLineFromConsoleWithValidation("Hour (HH:MM):", FieldToValidate.HOURS);

      if (!ctrl.isCenterOpenAt(center, hours)) {
        accepted = false;
        System.out.println(
            "\nVaccination Center is closed or does not accept appointments at selected time. Please enter other date.\n");
        continue;
      }

      Calendar appointmentDate = Calendar.getInstance();
      try {
        appointmentDate = CalendarUtils.parseDateTime(date, hours);
      } catch (ParseException e) {
        System.out.println("\n\nDate or Hour invalid.");
      }

      if (!ctrl.hasSlotAvailability(center, appointmentDate)) {
        accepted = false;
        System.out.println(
            "\nVaccination Center does not support more appointments at selected time. Please enter other date.\n");
        continue;
      }

      return appointmentDate;
    } while (!accepted);

    return null;
  }

  private boolean isUserEligibleForVaccine(VaccineType vaccineType) {
    if (checkIfUserHasTakenVaccineType(vaccineType)) {
      // ctrl.getVaccinesByType(vaccineType);
      // ctrl.checkAdministrationProcessForNextDose();
      // vacCenter = selectVaccinationCenterWithVaccineType(vaccineType);
      return false;
    } else {
      if (ctrl.checkAdministrationProcessForVaccineType(vaccineType)) {
        return true;
      } else {
        return false;
      }
    }
  }
}
