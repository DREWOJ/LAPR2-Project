package app.domain.model;

import java.util.Calendar;
import app.domain.shared.CalendarUtils;

/**
 * SNSUser model class.
 * 
 * @author Ricardo Moreira <1211285@isep.ipp.pt>
 */
public class SNSUser {

  // Max age a user can have
  private static final int MAX_AGE = 150;

  // Citizen Card
  private String citizenCard;

  // SNS number
  private String snsNumber;

  // SNS User name
  private String name;

  // SNS User birth day
  private Calendar birthDay;

  // SNS User gender
  private char gender;

  // SNS User phone number
  private String phoneNumber;

  // SNS User email
  private String email;

  // SNS User address
  private String address;

  /**
   * Constructor for SNSUser.
   * 
   * @param snsNumber
   * @param name
   * @param birthDay
   * @param gender
   * @param phoneNumber
   * @param email
   */
  public SNSUser(String citizenCard, String snsNumber, String name, Calendar birthDay, char gender, String phoneNumber, String email, String address) {
    validateBirthday(birthDay);
    validateCitizenCard(citizenCard);
    validateSNSNumber(snsNumber);
    validateName(name);
    validatePhoneNumber(phoneNumber);
    validateEmail(email);
    validateAddress(address);

    this.citizenCard = citizenCard.toUpperCase();
    this.snsNumber = snsNumber;
    this.name = name;
    this.birthDay = birthDay;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.address = address;
  }

  // Getters
  public String getCitizenCard() {
    return citizenCard;
  }

  public String getSnsNumber() {
    return snsNumber;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public char getGender() {
    return gender;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public Calendar getBirthDay() {
    return birthDay;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if (!(obj instanceof SNSUser)) return false;

    SNSUser other = (SNSUser) obj;

    // Fields email, phone number, citizen card & SNS number should be unique for
    // each SNS User
    if (this.email.equals(other.email)) return true;
    if (this.phoneNumber.equals(other.phoneNumber)) return true;
    if (this.citizenCard.equals(other.citizenCard)) return true;
    if (this.snsNumber.equals(other.snsNumber)) return true;

    return false;
  }

  /**
   * Validates the age given a birthday. Throws IllegalArgumentException.
   * 
   * @param birthDay
   */
  private static void validateBirthday(Calendar birthDay) {
    // checks if the birthday is in the future
    if (birthDay.after(Calendar.getInstance())) {
      throw new IllegalArgumentException("Birthday cannot be in the future");
    }

    // check if the birthday is more than MAX_AGE years ago
    int age = CalendarUtils.calculateAge(birthDay);

    if (age > MAX_AGE) {
      throw new IllegalArgumentException(String.format("Birthday cannot be more than %d years ago", MAX_AGE));
    }
  }

  private static void validateCitizenCard(String citizenCard) {
    // should follow the portuguese format
    // dddddddd d XXd (d - digit; X - letter)
    // regex: ^\d{7}[a-zA-Z]\d{2}[a-zA-Z]$
    if (!citizenCard.matches("^\\d{7}[a-zA-Z]\\d{2}[a-zA-Z]$")) {
      throw new IllegalArgumentException("Citizen Card is not valid");
    }
  }

  private static void validateSNSNumber(String snsNumber) {
    // should follow the portuguese format (9 digits)
    // regex: ^\d{9}$
    if (!snsNumber.matches("^\\d{9}$")) {
      throw new IllegalArgumentException("SNS Number is not valid");
    }
  }

  private static void validateName(String name) {
    // should not be empty
    // regex: ^.+$
    if (!name.matches("^.+$")) {
      throw new IllegalArgumentException("Name is not valid");
    }
  }

  private static void validatePhoneNumber(String phoneNumber) {
    // should have a + prefix
    // regex +\d{3} \d{9}
    if (!phoneNumber.matches("^\\+\\d{3} \\d{9}$")) {
      throw new IllegalArgumentException("Phone Number is not valid");
    }
  }

  private static void validateEmail(String email) {
    // regex:
    // ^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$
    if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
      throw new IllegalArgumentException("Email is not valid");
    }
  }

  private static void validateAddress(String address) {
    // should not be empty
    // regex: ^.+$
    if (!address.matches("^.+$")) {
      throw new IllegalArgumentException("Address is not valid");
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("SNS User name: %s\n", this.name));
    sb.append(String.format("Citizen card number: %s\n", this.citizenCard));
    sb.append(String.format("SNS number: %s\n", this.snsNumber));
    sb.append(String.format("Birthday: %s\n", CalendarUtils.calendarToString(this.birthDay)));
    sb.append(String.format("Gender: %s\n", this.gender == 'm' ? "Male" : "Female"));
    sb.append(String.format("Phone number: %s\n", this.phoneNumber));
    sb.append(String.format("Email: %s\n", this.email));
    sb.append(String.format("Address: %s\n", this.address));

    return sb.toString();
  }

}
