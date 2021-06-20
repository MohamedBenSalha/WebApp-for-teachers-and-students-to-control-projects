package de.unistuttgart.iste.pe2ws20.api.team;

import de.unistuttgart.iste.pe2ws20.api.project.Project;
import de.unistuttgart.iste.pe2ws20.api.registration.Registration;
import de.unistuttgart.iste.pe2ws20.api.students.Student;
import de.unistuttgart.iste.pe2ws20.api.Logic.Logic;
import de.unistuttgart.iste.pe2ws20.api.team.csv.CSVHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Tag(name = "Teams", description = "All implemented operations to manage the teams")
@RestController
public class TeamCtrl {
    private AtomicInteger counterTeams;
    private List<Team> teams;
    private Logic logic;


    @PostConstruct
    public void init() {
        teams = new ArrayList<>();
        counterTeams = new AtomicInteger(1);
        logic = new Logic();
    }


    @ApiResponse(responseCode = "200", description = "Team found and deleted successfully")
    @Operation(summary = "deletes all created teams", description = "Deletes the Teams from the server")
    @DeleteMapping("/team-allocation")
    public void deleteAllTeams() {
        counterTeams.set(1);
        teams.clear();

    }


    @ApiResponse(responseCode = "200", description = "Team found and deleted successfully")
    @ApiResponse(responseCode = "404", description = "Team not found")
    @Operation(summary = "deletes Team by ID", description = "Deletes the Team from the server")
    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@Parameter(description = "The id of the team to delete") @PathVariable("id") int id) {
        Team teamToDelete = getTeam(id);
        teams.remove(teamToDelete);
    }

    /**
     * First of all, the team  are sorted by the date of creation_.
     * Then the number of registrations is divided by teams, which gives
     * the minimum number of teams per project. Then a list with the IDs of
     * all projects is filled so that each project ID occurs the minimum
     * number of teams per project times. Then the teams are assigned.
     * If a team is assigned, then the ID is deleted from the list and so on.
     * At the end, the list is refilled once with all IDs and the remaining
     * teams get their projects with the same method.
     */
    @Operation(summary = "create all teams", description = "take all registrations and creates the teams")
    @ApiResponse(responseCode = "201", description = "Teams created successfully")
    @PostMapping("/team-allocation")
    @ResponseStatus(HttpStatus.CREATED)
    public List<TeamReturn> createTeams() {
        this.teams.clear();
        ArrayList<Registration> registrationsList = logic.getAllRegistrations();
        ArrayList<Project> projectsList = logic.getAllProjects();
        List<Integer> allProjectIDs = projectsList.stream().map(Project::getId).collect(Collectors.toList());

        int minTeamsPerProject = registrationsList.size() / projectsList.size();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < minTeamsPerProject; i++) {
            list.addAll(allProjectIDs);
        }
        for (int i = 0; i < registrationsList.size(); i++) {
            List<Integer> preferredProjectIDs = registrationsList.get(i).getPreferredProjects().stream().map(Project::getId).collect(Collectors.toList());
            int associatedProjectID = 0;
            String nameAssociatedProject = "";
            for (Integer preferredProjectID : preferredProjectIDs) {
                /*this will be executed only once because the list will not be empty anymore because there are
                 less registrations than the size of the list */
                if (list.isEmpty()) list.addAll(allProjectIDs);
                if (list.contains(preferredProjectID)) {
                    associatedProjectID = preferredProjectID;
                    int finalAssociatedProjectID = associatedProjectID;
                    nameAssociatedProject = projectsList.stream().filter(project -> project.getId() == finalAssociatedProjectID).map(Project::getName).collect(Collectors.toList()).get(0);
                    list.remove(Integer.valueOf(associatedProjectID));
                    break;
                }
            }
            Team team = new Team(counterTeams.getAndIncrement(), "Team " + (i + 1), associatedProjectID,
                    nameAssociatedProject, registrationsList.get(i).getStudents());
            teams.add(team);
        }
        return getAllTeams();
    }

    @Operation(summary = "Get teams", description = "Retrieves the list of all available teams on the server")
    @ApiResponse(responseCode = "200", description = "Projects found and returned successfully")
    @GetMapping("/teams")
    public List<TeamReturn> getAllTeams() {
        List<TeamReturn> teamsReturn = new ArrayList<>();
        for (Team team : this.teams) {
            TeamReturn teamReturn = new TeamReturn(team.getId(), team.getName(), team.getProjectId(),
                    team.getProjectName(), team.getStudents().size());
            teamsReturn.add(teamReturn);
        }
        teamsReturn.sort(Comparator.comparing(TeamReturn::getId));
        return teamsReturn;
    }

    @Operation(summary = "Finds team by ID", description = "Returns a single team whose ID was given")
    @ApiResponse(responseCode = "200", description = "team found successfully")
    @ApiResponse(responseCode = "404", description = "team not found")
    @GetMapping("/teams/{id}")
    public Team getTeam(@Parameter(description = "ID of the searched team") @PathVariable("id") long id) {
        for (Team team : teams) {
            if (team.getId() == id)
                return team;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Team with ID %s not found!", id));
    }

    @Operation(summary = "Get all teams as csv file", description = "Retrieves the list of all available teams on the server and parses" +
            "them into a csv file")
    @ApiResponse(responseCode = "200", description = "csv file created successfully")
    @GetMapping("/csv-downloads/teams")
    public ResponseEntity<Resource> downloadCSVTeams() {
        String filename = "teams.csv";
        InputStreamResource file = new InputStreamResource(CSVHelper.teamsToCSV(this.teams));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }


    @Operation(summary = "Updates team by ID", description = "modifies the data of the given team")
    @ApiResponse(responseCode = "200", description = "team has been successfully updated")
    @ApiResponse(responseCode = "404", description = "team not found")
    @PutMapping("/teams/{id}")
    public Team updateTeam(@Parameter(description = "The id of the team to modify") @PathVariable("id") Integer id,
                           @Parameter(description = "Contains the modified data of the team") @Valid @RequestBody Map<String, Object> team) {
        Team teamToUpdate = getTeam(id);
        Team team1 = new Team(id, (String) team.get("name"), teamToUpdate.getProjectId(), teamToUpdate.getProjectName(),
                (List<Student>) team.get("students"));
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId() == id)
                this.teams.set(i, team1);
        }
        return team1;
    }

}



