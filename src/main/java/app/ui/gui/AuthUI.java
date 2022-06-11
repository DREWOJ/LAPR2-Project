package app.ui.gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import app.controller.AuthController;
import app.domain.shared.Constants;
import app.domain.shared.MenuFXMLPath;
import app.service.FormatVerifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pt.isep.lei.esoft.auth.mappers.dto.UserRoleDTO;

public class AuthUI implements Initializable, IGui {
  private ApplicationUI mainApp;
  private AuthController ctrl;
  private int maxAttempts = Constants.MAX_OF_PASSWORD_TRIES;

  @FXML
  private TextField txtEmail;
  @FXML
  private PasswordField txtPwd;

  @FXML
  private Button btnLogin;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ctrl = new AuthController();
  }

  @Override
  public void setMainApp(ApplicationUI mainApp) {
    this.mainApp = mainApp;
  }

  @FXML
  void btnExit(ActionEvent event) {
    exitApplication();
  }

  private void exitApplication() {
    mainApp.getStage().close();
  }

  @FXML
  void btnLogin(ActionEvent event) {
    // test
    login();
  }

  private void displayInvalidCredentialsAlert() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Login Failed");
    alert.setHeaderText("Please insert your email and password again.");
    alert.setContentText(String.format("You have %d attempts left.", maxAttempts));
    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) Logger.getLogger(getClass().getName()).log(Level.INFO, "Login failed");
    });

    resetTextFields();
  }

  @FXML
  void checkEnableLoginButton() {
    boolean isEmailValid = FormatVerifier.validateEmail(txtEmail.getText());

    if (isEmailValid && !txtPwd.getText().isEmpty()) btnLogin.setDisable(false);
    else btnLogin.setDisable(true);
  }

  private void displayMaxAttemptsReachedAlert() {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Login Failed");
    alert.setHeaderText("Maximum number of attempts reached.");
    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) Logger.getLogger(getClass().getName()).log(Level.INFO, "Login failed");
    });
  }

  @FXML
  void onKeyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) login();
  }

  private void login() {
    String email = txtEmail.getText();
    String pwd = txtPwd.getText();

    if (!ctrl.doLogin(email, pwd)) {
      if (maxAttempts == 0) {
        displayMaxAttemptsReachedAlert();
        exitApplication();
        return;
      }

      displayInvalidCredentialsAlert();
      maxAttempts--;
      return;
    }

    UserRoleDTO role = ctrl.getUserRoles().get(0);
    String menuFXML = getMenuWithRoleFXML(role);

    if (menuFXML == null) {
      Logger.getLogger(AuthUI.class.getName()).log(Level.SEVERE, "No menu found for role: " + role.getDescription());
      return;
    }

    try {
      try {
        RoleUI gui = (RoleUI) mainApp.replaceSceneContent(menuFXML);
        gui.setMainApp(mainApp);
      } catch (Error e) {
        resetTextFields();
        Logger.getLogger(ApplicationUI.class.getName()).log(Level.SEVERE, "Coordinator has no center.");
      }

    } catch (Exception e) {
      Logger.getLogger(ApplicationUI.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  private void resetTextFields() {
    txtEmail.setText("");
    txtPwd.setText("");
    txtEmail.requestFocus();
  }

  private String getMenuWithRoleFXML(UserRoleDTO role) {
    if (role.getId().equals(MenuFXMLPath.RECEPTIONIST.name())) return MenuFXMLPath.SELECT_CENTER.toString();
    if (role.getId().equals(MenuFXMLPath.NURSE.name())) return MenuFXMLPath.SELECT_CENTER.toString();

    for (MenuFXMLPath path : MenuFXMLPath.values())
      if (path.name().equals(role.getId())) return path.toString();

    return null;
  }
}
