package app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import app.domain.model.Address;
import app.domain.model.AdminProcess;
import app.domain.model.Appointment;
import app.domain.model.Company;
import app.domain.model.DoseInfo;
import app.domain.model.Employee;
import app.domain.model.SNSUser;
import app.domain.model.VaccinationCenter;
import app.domain.model.Vaccine;
import app.domain.model.VaccineType;
import app.domain.model.store.EmployeeStore;
import app.domain.model.store.SNSUserStore;
import app.domain.model.store.VaccinationCenterStore;
import app.domain.model.store.VaccineTypeStore;
import app.domain.shared.Gender;
import app.dto.VaccinationCenterListDTO;
import app.dto.VaccineTypeDTO;
import app.mapper.AppointmentInsertMapper;
import app.mapper.VaccinationCenterMapper;
import app.mapper.VaccineTypeMapper;
import app.service.CalendarUtils;

public class ScheduleVaccineControllerTest {
  Company company = new Company("designation", "12345");
  ScheduleVaccineController controller = new ScheduleVaccineController(company);
  String resourceName = "Appointment";
  private VaccinationCenter vaccinationCenter;
  private VaccineType vaccineType;
  private SNSUser user;
  private Appointment appointment;
  private Calendar calendar;
  private boolean sms = true;
  private VaccinationCenterStore vacStore;
  private VaccineTypeStore vaccineTypeStore;
  private SNSUserStore snsUserStore;
  private VaccinationCenterListDTO centerDto;
  private VaccineTypeDTO vaccineTypeDTO;

  @Before
  public void setUp() throws ParseException {
    EmployeeStore employeeStore = company.getEmployeeStore();
    Employee coordinator =
        employeeStore.createEmployee("name", "+351212345678", "email@email.com", new Address("street", 1, "1-1", "city"), "00000000", "COORDINATOR");
    employeeStore.saveEmployee(coordinator);

    vacStore = company.getVaccinationCenterStore();
    vaccinationCenter = vacStore.createHealthCareCenter("name", new Address("street", 1, "1-1", "city"), "email@email.com", "+351212345678", "+351212345678",
        "http://www.com", "20:00", "21:00", 5, 5, coordinator, "ages", "ags");
    vacStore.saveVaccinationCenter(vaccinationCenter);

    centerDto = VaccinationCenterMapper.toDto(vaccinationCenter);

    Calendar age = Calendar.getInstance();
    age.add(Calendar.YEAR, -18);

    snsUserStore = company.getSNSUserStore();
    user =
        new SNSUser("00000000", "123456789", "name", age.getTime(), Gender.MALE, "+351212345678", "email@email.com", new Address("street", 1, "11-11", "city"));
    snsUserStore.saveSNSUser(user);

    calendar = CalendarUtils.parseDateTime(new Date(), "20:40");

    vaccineTypeStore = company.getVaccineTypeStore();
    vaccineType = new VaccineType("12345", "TEST", "TEST_TECHNOLOGY");
    vaccineTypeStore.saveVaccineType(vaccineType);

    vaccineTypeDTO = VaccineTypeMapper.toDto(vaccineType);

    Vaccine vaccine = new Vaccine("designation", "12345", "brand", vaccineType);
    AdminProcess adminProcess = new AdminProcess(10, 19, 1);
    adminProcess.addDoseInfo(new DoseInfo(200, 10));
    vaccine.addAdminProc(adminProcess);
    company.getVaccineStore().saveVaccine(vaccine);

    appointment = new Appointment(user, calendar, vaccinationCenter, vaccineType, sms);
    AppointmentInsertMapper.toDto(appointment);
  }

  /**
   * Check getResourceName method is working properly
   */
  @Test
  public void ensureGetResourceNameIsWorkingCorrectly() {
    assertEquals(resourceName, controller.getResourceName());
  }

  /**
   * Check that it is possible to create an appointment
   */
  @Test
  public void ensureItIsPossibleToCreateAppointment() {
    controller.createAppointment(user.getSnsNumber(), new Date(), "20:30", centerDto, vaccineTypeDTO, true);
  }

  @Test
  public void ensureGetSuggestedVaccineTypeIsWorkingCorrectly() {
    assertNotNull(controller.getSuggestedVaccineType());
  }

  @Test
  public void ensureToStringIsWorking() {
    controller.createAppointment(user.getSnsNumber(), new Date(), "20:30", centerDto, vaccineTypeDTO, true);
    assertNotNull(controller.stringifyData());
  }

  @Test
  public void ensureSaveIsWorking() {
    controller.createAppointment(user.getSnsNumber(), new Date(), "20:30", centerDto, vaccineTypeDTO, true);
    controller.save();
  }

  @Test
  public void ensureGetListOfVaccinationCentersWithVaccineType() {
    List<VaccinationCenterListDTO> list = controller.getListOfVaccinationCentersWithVaccineType(vaccineTypeDTO);
    assertNotNull(list);
  }

  @Test
  public void ensureGetListOfVaccineTypesIsWorking() {
    List<VaccineTypeDTO> list = controller.getListOfVaccineTypes();
    assertNotNull(list);
  }

  @Test
  public void ensureGetRegisteredObjectIsWorking() {
    controller.createAppointment(user.getSnsNumber(), new Date(), "20:30", centerDto, vaccineTypeDTO, true);
    controller.save();
    assertNotNull(controller.getRegisteredObject());
  }
}
