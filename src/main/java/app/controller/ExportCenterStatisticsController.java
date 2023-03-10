package app.controller;

import java.util.Calendar;
import java.util.LinkedHashMap;
import app.domain.model.Company;
import app.domain.model.FullyVaccinatedData;
import app.domain.model.VaccinationCenter;
import app.exception.NotAuthorizedException;
import app.service.FileUtils;
import app.session.EmployeeSession;

/**
 * ExportCenterStatisticsController
 * 
 * @author André Barros <1211299@isep.ipp.pt>
 */
public class ExportCenterStatisticsController {
  private EmployeeSession session;
  private FullyVaccinatedData exporter;
  private LinkedHashMap<Calendar, Integer> dataMap;

  /**
   * ExportCenterStatisticsController constructor
   * 
   * @param company the company
   * @param coordinatorSession the coordinator session
   * @throws NotAuthorizedException exception if coordinator is not logged in
   */
  public ExportCenterStatisticsController(Company company, EmployeeSession coordinatorSession) throws NotAuthorizedException {
    if (!coordinatorSession.hasCenter()) throw new NotAuthorizedException("Coordinator is not logged in.");
    this.session = coordinatorSession;
  }

  /**
   * Creates fullyVaccinatedData
   * 
   * @param filePath the file path to save the statistics
   * @param start the start date of statistics
   * @param end the end date of statistics
   * @return fullyVaccinatedData object
   */
  public void createFullyVaccinatedData(String filePath, Calendar start, Calendar end) {
    VaccinationCenter center = session.getVaccinationCenter();

    if (end.before(start)) throw new IllegalArgumentException("Invalid date interval!");

    this.exporter = new FullyVaccinatedData(filePath, start, end, center);
  }

  /**
   * Gathers all the information needed to export to a file the statistics
   * 
   * @param exporter FullyVaccinatedData Object
   * @return hashMap will all the data needed
   */
  public void generateFullyVaccinatedUsersData() {
    this.dataMap = this.exporter.getFullyVaccinatedUsersPerDayMap();
  }

  /**
   * Saves data
   * 
   * @param dataMap the hashMap will all information
   */
  public boolean saveData(String fileName) {
    String content = exportFileString();
    return FileUtils.writeToFile(fileName, content);
  }

  /**
   * Gets data formatted to CSV file
   * 
   * @return data ready to go to the file
   */
  public String exportFileString() {
    return exporter.toExportFileString(this.dataMap);
  }

  /**
   * Gets the data
   * 
   * @return LinkedHashMap with all the data
   */
  public LinkedHashMap<Calendar, Integer> getData() {
    return this.dataMap;
  }
}
