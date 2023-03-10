package app.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;
import app.domain.shared.Gender;
import app.utils.Time;

public class WaitingRoomTest {
  Arrival arrival;
  WaitingRoom waitingRoom;
  Appointment appointment;

  @Before
  public void setup() {
    waitingRoom = new WaitingRoom();
    SNSUser snsUser = new SNSUser("00000000", "123456789", "name", Calendar.getInstance().getTime(), Gender.MALE, "+351212345678", "email@email.com",
        new Address("street", 1, "11-11", "city"));
    Employee coordinator =
        new Employee("123456789", "name", "+351212345678", "email@email.com", new Address("street", 1, "11-11", "city"), "00000000", "COORDINATOR");

    Time openingHours = new Time(8, 0);
    Time closingHours = new Time(19, 0);
    Slot slot = new Slot(5, 10);

    VaccinationCenter center = new HealthCareCenter("Centro Vacinação Porto", new Address("street", 1, "11-11", "city"), "vacinacaoporto@gmail.com",
        "+351912345678", "+351223456789", "https://www.centrovacinaoporto.com", openingHours, closingHours, slot, coordinator, "a", "a");
    VaccineType vaccineType = new VaccineType("12345", "description", "technology");

    appointment = new Appointment(snsUser, Calendar.getInstance(), center, vaccineType, true);
  }

  @Test
  public void ensureEmptyWaitingRoomIsEmpty() {
    assertEquals(0, waitingRoom.size());
  }

  @Test
  public void ensureWaitingRoomSizeIsWorking() {
    Arrival arrival = waitingRoom.createArrival(appointment, Calendar.getInstance());
    assertEquals(0, waitingRoom.size());

    waitingRoom.saveArrival(arrival);
    assertEquals(1, waitingRoom.size());
  }

  @Test
  public void ensureHasSnsUserArrivedTodayIsWorking() {
    Arrival arrival = waitingRoom.createArrival(appointment, Calendar.getInstance());

    waitingRoom.saveArrival(arrival);
    assertTrue(waitingRoom.hasSNSUserArrivedToday(appointment.getSnsUser()));
  }

  @Test
  public void ensureHasSnsUserArrivedTodayIsWorking2() {
    SNSUser snsUser2 = new SNSUser("00000000", "123456789", "name", Calendar.getInstance().getTime(), Gender.MALE, "+351212345678", "email@email.com",
        new Address("street", 1, "11-11", "city"));
    assertFalse(waitingRoom.hasSNSUserArrivedToday(snsUser2));
  }

  @Test
  public void ensureRemoveUserIsWorking() {
    Arrival arrival = waitingRoom.createArrival(appointment, Calendar.getInstance());

    waitingRoom.saveArrival(arrival);

    assertEquals(waitingRoom.size(), 1);

    waitingRoom.removeUser(appointment.getSnsUser());

    assertEquals(waitingRoom.size(), 0);
  }

  @Test
  public void ensureRemoveLastArrivalIsWorking() {
    Arrival arrival = waitingRoom.createArrival(appointment, Calendar.getInstance());

    waitingRoom.saveArrival(arrival);

    assertEquals(waitingRoom.size(), 1);

    waitingRoom.removeLastArrival();

    assertEquals(waitingRoom.size(), 0);
  }
}
