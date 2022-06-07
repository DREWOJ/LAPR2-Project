package app.controller;

import java.util.Date;
import java.util.Map;
import app.domain.model.Company;
import app.domain.model.CsvExporter;
import app.domain.model.VaccinationCenter;
import app.exception.NotAuthorizedException;
import app.service.FullyVaccinatedData;
import app.session.EmployeeSession;

public class ExportCenterStatisticsController {
    private Company company;
    private EmployeeSession session;
    private FullyVaccinatedData exporter;
    private Map<String, Integer> data;

    public ExportCenterStatisticsController(Company company, EmployeeSession coordinatorSession) throws NotAuthorizedException {
        if (!coordinatorSession.hasCenter()) throw new NotAuthorizedException("Coordinator is not logged in");
        this.session = coordinatorSession;
        this.company = company;
    }

    public FullyVaccinatedData createCsvExporterData(String filePath, Date start, Date end) {
        VaccinationCenter center = session.getVaccinationCenter();

        FullyVaccinatedData exporter = new FullyVaccinatedData(filePath, start, end, center);

        return exporter;
    }

    public Map<String, Integer> generateFullyVaccinatedUsersInterval() {
        data = exporter.getFullyVaccinatedUsersPerDayMap();
        return data;
    }

    public void saveData(String filePath, Map data) {
        CsvExporter csvExporter = new CsvExporter(filePath);
        csvExporter.writeToFile(data);
    }
}
