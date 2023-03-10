package app.ui.console;

import java.util.List;
import app.controller.ListUsersInWaitingRoomController;
import app.dto.ArrivalDTO;
import app.exception.NotAuthorizedException;
import app.session.EmployeeSession;

public class ListUsersInWaitingRoomUI implements Runnable {
  ListUsersInWaitingRoomController controller;

  public ListUsersInWaitingRoomUI(EmployeeSession nurseSession) {
    try {
      this.controller = new ListUsersInWaitingRoomController(nurseSession);
    } catch (NotAuthorizedException e) {
      System.out.println("Nurse is not logged in");
      return;
    }
  }

  public void run() {
    String data = getWaitingRoomDataForNurseCenter();

    if (data == null) System.out.println("\nThere are no users waiting in the waiting Room");
    else System.out.println(data);
  }

  private String getWaitingRoomDataForNurseCenter() {
    List<ArrivalDTO> waitingRoom = controller.getWaitingRoomListFromNurseCenter();
    if (waitingRoom.size() == 0) return null;

    StringBuilder sb = new StringBuilder();
    sb.append("\n\nWaiting Room:");
    sb.append("\n\n");

    for (int i = 0; i < waitingRoom.size(); i++)
      sb.append(String.format("%d. %n%s%n%n", i + 1, waitingRoom.get(i)));

    return sb.toString();
  }
}
