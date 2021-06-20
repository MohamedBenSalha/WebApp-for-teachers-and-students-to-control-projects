package de.unistuttgart.iste.pe2ws20.api.team.csv;

import de.unistuttgart.iste.pe2ws20.api.students.Student;
import de.unistuttgart.iste.pe2ws20.api.team.Team;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {
    public static ByteArrayInputStream teamsToCSV(final List<Team> teams) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            List<String> data = Arrays.asList("full_name","ilias_name","email","team_name","project_name");
            csvPrinter.printRecord(data);
            for (Team team : teams) {
                List<Student> studentsTeam = team.getStudents();
                for (Student students : studentsTeam) {
                    data = Arrays.asList(
                            students.getName() + " " +students.getPrename(),
                            students.getIliasName(),
                            students.getEmail(),
                            team.getName(),
                            team.getProjectName()
                    );
                    csvPrinter.printRecord(data);
                }

            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
