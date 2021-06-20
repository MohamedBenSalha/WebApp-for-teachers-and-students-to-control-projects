package de.unistuttgart.iste.pe2ws20.api.registration;

import de.unistuttgart.iste.pe2ws20.api.Logic.Logic;
import de.unistuttgart.iste.pe2ws20.api.project.Project;
import de.unistuttgart.iste.pe2ws20.api.students.Student;
import de.unistuttgart.iste.pe2ws20.api.team.Team;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Tag(name = "Registration", description = "All implemented operations to manage the registrations")
@RestController
public class RegistrationCtrl {


    private AtomicInteger counterRegistration;
    private List<Registration> registrations;

    private Logic logic;


    @PostConstruct
    public void init() {
        registrations = new ArrayList<>();
        counterRegistration = new AtomicInteger(1);
        logic = new Logic();

    }

    @Operation(summary = "Get Registrations", description = "Retrieves the list of all available registrations on the server")
    @ApiResponse(responseCode = "200", description = "Registrations found and returned successfully")
    @GetMapping("/registrations")
    public List<RegistrationReturnAll> getRegistrations() {
        List<RegistrationReturnAll> registrationReturnAlls = new ArrayList<>();

        for (final Registration registration : this.registrations) {
            List<Integer> preferredProjectIds = registration.getPreferredProjects().stream().map(Project::getId).collect(Collectors.toList());
            RegistrationReturnAll registrationReturnAll = new RegistrationReturnAll(registration.getId(),
                    preferredProjectIds, registration.getStudents().size());
            registrationReturnAlls.add(registrationReturnAll);
        }
        registrationReturnAlls.sort(Comparator.comparing(RegistrationReturnAll::getId));
        return registrationReturnAlls;
    }

    @Operation(summary = "Finds registration by ID", description = "Returns a single registration whose ID was given")
    @ApiResponse(responseCode = "200", description = "registration found successfully")
    @ApiResponse(responseCode = "404", description = "registration not found")
    @GetMapping("/registrations/{id}")
    public RegistrationReturnByID getRegistration(@Parameter(description = "ID of the searched registration") @PathVariable("id") long id) {

        for (final Registration registration : registrations) {
            if (registration.getId() == id) {
                List<Integer> preferredProjectIds = registration.getPreferredProjects().stream().map(Project::getId).collect(Collectors.toList());
                return new RegistrationReturnByID(registration.getId(), preferredProjectIds, registration.getStudents(),registration.getCreated().getTime());
            }
        }


        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Registration with ID %s not found!", id));
    }

    /**
     * gets as parameter a JSON object of type Registration. This object has as attribute the students,
     * which are parsed and then stored in an array. This object also has as attributes the list of IDs
     * #of the preferred projects. All the projects available in the server are retrieved and then the
     * projects whose IDs match those in the preferred projects are inserted in a list passed in the
     * registration object to be created.
     *
     * @param requestBodyRegistration ii as Map<String, Object> so that it is considered and classified
     *                     as a JSON object, making it easier to get the attributes out of it
     * @return the added Registration Object
     */
    @Operation(summary = "Adds a registration project", description = "The details of the registration will be extracted from the request body ")
    @ApiResponse(responseCode = "201", description = "registration created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request body")
    @PostMapping("/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public Registration addRegistration(@Parameter(description = "From the automatically JSON parsed Request Body a registration " +
            "will be created. The request Body contains all necessary attributes which are needed to successfully create a registration ")
                                        @Valid @RequestBody Map<String, Object> requestBodyRegistration) {

        ArrayList<Student> arrayOfStudents = logic.getStudents(requestBodyRegistration);
        ArrayList<Project> preferredProjects = logic.getPreferredProjects(requestBodyRegistration);
        if (!logic.checkStudentsValidity(arrayOfStudents))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Students data does not match the required one!");

        if (!logic.checkProjectsValidity(preferredProjects))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Projects data does not match the required one!");


        Registration registration = new Registration(preferredProjects,
                arrayOfStudents);
        registration.setId(counterRegistration.getAndIncrement());
        registrations.add(registration);
        return registration;

    }


    @Operation(summary = "Updates registration by ID", description = "modifies the data of the given registration")
    @ApiResponse(responseCode = "200", description = "registration has been successfully updated")
    @ApiResponse(responseCode = "404", description = "registration not found")
    @PutMapping("/registrations/{id}")
    public RegistrationReturnByID updateRegistration(@Parameter(description = "The id of the registration to modify") @PathVariable("id") Integer id,
                                                     @Parameter(description = "Contains the modified data of the registration")
                                                     @Valid @RequestBody Map<String, Object> registration) {

        RegistrationReturnByID registrationToUpdate = getRegistration(id);
        ArrayList<Student> arrayOfStudents = logic.getStudents(registration);
        ArrayList<Project> preferredProjects = logic.getPreferredProjects(registration);
        registrationToUpdate.setPreferredProjectIds((ArrayList<Integer>) registration.get("preferredProjectIds"));
        registrationToUpdate.setStudents(arrayOfStudents);
        // removes the registration with the given ID and then adds the update one
        Registration registration1 = new Registration(preferredProjects,
                arrayOfStudents);
        registration1.setId(id);
        registrations = registrations.stream().filter(project11 -> project11.getId() != registration1.getId()).collect(Collectors.toList());
        registrations.add(registration1);
        return registrationToUpdate;


    }

    @Operation(summary = "Get Registrations", description = "Retrieves the list of all available registrations on the server (special format)")
    @ApiResponse(responseCode = "200", description = "Registrations found and returned successfully")
    @GetMapping("/submittedRegistrations")
    public List<Registration> getSubmittedRegistrations() {
        return registrations;
    }

    @ApiResponse(responseCode = "200", description = "Registration found and deleted successfully")
    @ApiResponse(responseCode = "404", description = "Registration not found")
    @Operation(summary = "deletes registration by ID", description = "Deletes the registration from the server")
    @DeleteMapping("/registrations/{id}")
    public void deleteRegistration(@Parameter(description = "The id of the registration to delete") @PathVariable("id") int id) {
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getId() == id) {
                this.registrations.remove(i);
                return ;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Registration with ID %s not found!", id));
    }
}


