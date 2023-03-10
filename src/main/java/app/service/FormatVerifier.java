package app.service;

import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import app.domain.shared.Constants;
import app.utils.Time;

/**
 * Format Verifier with all the rules for the application.
 * 
 * @author André Barros <1211299@isep.ipp.pt>
 * @author Tomás Lopes <1211289@isep.ipp.pt>
 */
public final class FormatVerifier {
  /**
   * Private constructor to avoid instantiation.
   */
  private FormatVerifier() {}

  /**
   * From a field String, returns the method that validates it.
   * 
   * @param field the field to validate
   * @return
   */
  public static Method getValidationMethodForField(String field) {
    Class[] argList = {String.class};

    String methodName = "validate";

    String fieldName = StringUtils.join(field.split(" "), "");
    methodName += fieldName;

    try {
      return Class.forName("app.service.FormatVerifier").getMethod(methodName, argList);
    } catch (Throwable e) {
      System.err.println(e);
      return null;
    }
  }

  /**
   * <p>
   * Validates if the given string is a citizen card number. ~~Follows the rules for portuguese citizens cards.~~
   * </p>
   * 
   * <p>
   * ~~Working example: 000000000ZZ4~~
   * </p>
   * 
   * <p>
   * For reference:
   * https://www.autenticacao.gov.pt/documents/20126/115760/Valida%C3%A7%C3%A3o+de+N%C3%BAmero+de+Documento+do+Cart%C3%A3o+de+Cidad%C3%A3o.pdf/bdc4eb37-7316-3ff4-164a-f869382b7053
   * </p>
   * 
   * @param cc The citizen card number to be verified.
   * @return True if the cc is valid, false otherwise.
   */
  public static boolean validateCitizenCard(String cc) {
    CCFormatVerifier ccVerifier = new CCFormatVerifier();

    try {
      return ccVerifier.validate(cc);
    } catch (IllegalArgumentException e) {
      // System.out.println(e);
      return false;
    }
  }

  /**
   * <p>
   * Validates if the given string is a email. Must follow the structure <user>@<domain>. Ex.: example@email.com
   * </p>
   * 
   * <p>
   * For reference: https://www.emailregex.com
   * </p>
   * 
   * @param expression The expression to be validated.
   * @return True if the expression is a valid email, false otherwise.
   */
  public static boolean validateEmail(String expression) {
    return expression.matches(
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
  }

  /**
   * <p>
   * Validates if the given string is a phone number. Follows the patterns +{extension}{number} or {number} Example:
   * +351223456789.
   * </p>
   * <p>
   * Between the extension and the number there is no space. The first digit after the extension must be a 2 or a 9.
   * </p>
   * 
   * @param expression The expression to be validated.
   * @return True if the expression is a valid phone number, false otherwise.
   */
  public static boolean validatePhoneNumber(String expression) {
    return expression.matches("\\+[0-9]{1,3}[2|3|8|9][0-9]{8}") || expression.matches("[0-9]{9}");
  }

  /**
   * Follows the same rules as the phone number.
   *
   * +{extension}{number} Example: +351223456789 The first digit after the extension must be a 2 or a 9.
   * 
   * @param expression The expression to be validated.
   * @return True if the expression is a valid fax number, false otherwise.
   */
  public static boolean validateFaxNumber(String expression) {
    return validatePhoneNumber(expression);
  }

  /**
   * Validates if the given string is a valid URL.
   * 
   * For reference: https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
   * 
   * @param expression The expression to be validated.
   * @return True if the expression is a valid URL, false otherwise.
   */
  public static boolean validateURL(String expression) {
    return expression.matches("http[s]?://(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");
  }

  /**
   * Validates if the given string is a valid SNS Number.
   * 
   * The only rule is that the expression must be composed by 9 digits
   * 
   * @param expression The expression to be validated.
   * @return True if the expression is a valid SNS Number, false otherwise.
   */
  public static boolean validateSNSNumber(String expression) {
    return expression.matches("[0-9]{9}");
  }

  /**
   * Validates if the given string is a valid Vaccine Code.
   * 
   * The only rule is that the expression must be composed by 5 digits
   * 
   * @param expression The expression to be validated.
   * @return True if the expression is a valid Vaccine Code, false otherwise.
   */
  public static boolean validateVaccineCode(String expression) {
    return expression.matches("[0-9]{" + Constants.VACCINE_TYPE_CODE_LENGTH + "}");
  }

  public static boolean validateHours(String expression) {
    try {
      new Time(expression);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static boolean validatePostalCode(String expression) {
    return expression.matches("[0-9]{1,}[-]?[0-9]{1,}");
  }
}
