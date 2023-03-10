package app.domain.model;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import pt.isep.lei.esoft.auth.AuthFacade;

/**
 * Employee's Tests
 * 
 * @author Tomás Russo <1211288@isep.ipp.pt>
 */

public class EmployeeTest {
  AuthFacade authFacade;

  @Before
  public void setUp() {
    this.authFacade = new AuthFacade();
  }

  /**
   * Check that it is not possible to create an instance of the Employee class with null values.
   * 
   * @throws Exception
   */
  @Test(expected = IllegalArgumentException.class)
  public void ensureNullIsNotAllowed() {
    new Employee(null, null, null, null, null, null, null);
  }

  /**
   * Check that it is not possible to create an instance of the Employee class with empty values.
   * 
   * @throws Exception
   */
  @Test(expected = IllegalArgumentException.class)
  public void ensureEmptyIsNotAllowed() {
    new Employee("", "", "", "", null, "", "");
  }

  /**
   * Check that it is not possible to create an instance of the Employee class with an invalid email.
   * 
   * @throws Exception
   */
  @Test(expected = IllegalArgumentException.class)
  public void ensureValidEmail() {
    String invalidEmail = "joana";

    new Employee("0000000001", "Joana Maria", "+351916478865", invalidEmail, new Address("street", 1, "11-11", "city"), "30365258", "Nurse");
  }

  /**
   * Check that it is not possible to create an instance of the Employee class with an invalid phone number, which
   * contains an invalid length.
   * 
   * @throws Exception
   */
  @Test(expected = IllegalArgumentException.class)
  public void ensureValidPhoneNumberWithValidLength() {
    String invalidPhoneNumber = "+3519164788687897845";

    new Employee("000000001", "Joana Maria", invalidPhoneNumber, "joanamaria@gmail.com", new Address("street", 1, "11-11", "city"), "30365258", "Nurse");
  }

  /**
   * Check that it is not possible to create an instance of the Employee class with an invalid phone number, which
   * contains letters.
   * 
   * @throws Exception
   */
  @Test(expected = IllegalArgumentException.class)
  public void ensureValidPhoneNumberWithoutLetters() {
    String invalidPhoneNumber = "+3519164foe";

    new Employee("000000001", "Joana Maria", invalidPhoneNumber, "joanamaria@gmail.com", new Address("street", 1, "11-11", "city"), "30365258", "Nurse");
  }

  /**
   * 
   * Check that it is not possible to create an instance of the Employee class with an invalid Citizen Card number.
   * 
   * @throws Exception
   */
  @Test(expected = IllegalArgumentException.class)
  public void ensureValidCCNumber() {
    String invalidCCNumber = "30325126512";

    new Employee("000000001", "Joana Maria", "+351916478865", "joanamaria@gmail.com", new Address("street", 1, "11-11", "city"), invalidCCNumber, "Nurse");
  }

  @Test
  public void ensureIsPossibleToCreateEmployee() {
    Employee instance =
        new Employee("000000001", "Joana Maria", "+351123456789", "email@email.com", new Address("street", 1, "11-11", "city"), "12345678", "NURSE");

    assertNotNull(instance);
  }
}
