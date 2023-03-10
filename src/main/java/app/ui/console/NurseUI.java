package app.ui.console;

import java.util.ArrayList;
import java.util.List;
import app.session.EmployeeSession;
import app.ui.console.utils.Utils;

public class NurseUI extends EmployeeWithCenterUI {
  public NurseUI() {
    super();
  }

  public void callback() {
    List<MenuItem> options = new ArrayList<MenuItem>();

    EmployeeSession nurseSession = super.getEmployeeSession();

    options
        .add(new MenuItem("Get Users in Waiting Room", new ListUsersInWaitingRoomUI(nurseSession)));

    int option = 0;

    do {
      System.out.printf("%nNurse Vaccination Center: %s", super.getCurrentVaccinationCenter());
      option = Utils.showAndSelectIndex(options, "\n\nNurse Menu:");

      if ((option >= 0) && (option < options.size())) options.get(option).run();
    } while (option != -1);
  }
}
