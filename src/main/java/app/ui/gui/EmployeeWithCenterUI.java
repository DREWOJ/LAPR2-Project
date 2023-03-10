package app.ui.gui;

import java.util.List;
import app.controller.App;
import app.controller.SelectEmployeeVaccinationCenterController;
import app.domain.shared.HelpText;
import app.domain.shared.MenuFXMLPath;
import app.dto.VaccinationCenterListDTO;
import app.ui.gui.utils.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import pt.isep.lei.esoft.auth.mappers.dto.UserRoleDTO;

public class EmployeeWithCenterUI extends EmployeeRoleUI {
  private SelectEmployeeVaccinationCenterController ctrl = new SelectEmployeeVaccinationCenterController(App.getInstance().getCompany(), employeeSession);

  @FXML
  private ListView<VaccinationCenterListDTO> lstCenters;

  @FXML
  private Button btnConfirm;

  private VaccinationCenterListDTO selectedCenter;

  @Override
  void init(ApplicationUI mainApp) {
    this.setMainApp(mainApp);
    List<VaccinationCenterListDTO> centers = ctrl.getVaccinationCentersList();
    lstCenters.getItems().addAll(centers);
  }

  @FXML
  void checkEnableButton(Event e) {
    btnConfirm.setDisable(false);

    selectedCenter = lstCenters.getSelectionModel().getSelectedItem();
  }

  @FXML
  void btnConfirm(Event event) {
    ctrl.selectVaccinationCenter(selectedCenter, employeeSession);
    this.redirectToRoleMenu();
  }

  public String getUIRoleName() {
    return "Employee";
  }

  @Override
  void handleHelp(ActionEvent event) {
    Utils.showHelp("Employee Help", HelpText.EMPLOYEE_WITH_CENTER);
  }

  private void redirectToRoleMenu() {
    UserRoleDTO role = App.getInstance().getCurrentUserSession().getUserRoles().get(0);

    String fxmlPath = null;

    for (MenuFXMLPath path : MenuFXMLPath.values())
      if (path.name().equals(role.getId())) fxmlPath = path.toString();

    try {
      RoleUI gui = (RoleUI) mainApp.replaceSceneContent(fxmlPath);
      gui.init(mainApp);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
