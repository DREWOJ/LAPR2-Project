package app.domain.shared;

public enum MenuFXMLPath {
  RECEPTIONIST("/fxml/ReceptionistMenu.fxml"), COORDINATOR("/fxml/CoordinatorMenu.fxml"), NURSE("/fxml/NurseMenu.fxml"), SNS_USER(
      "/fxml/SNSUserMenu.fxml"), ADMINISTRATOR("/fxml/AdminMenu.fxml"), SELECT_CENTER("/fxml/SelectCenter.fxml");

  private String path;


  private MenuFXMLPath(String path) {
    this.path = path;
  }

  @Override
  public String toString() {
    return path;
  }
}
