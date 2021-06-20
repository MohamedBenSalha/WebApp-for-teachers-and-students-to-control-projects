package de.unistuttgart.iste.pe2ws20.api.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
@Tag(name = "Projects", description = "All implemented operations to manage the projects")
@RestController
public class ProjectCtrl {

    private AtomicInteger counter;
    private List<Project> projects;


    @PostConstruct
    public void init() {
        counter = new AtomicInteger(1);
        projects = new ArrayList<Project>();
      // Dummy objects are created and added so that the view is not empty.
        Project project1 = new Project(counter.getAndIncrement(), "Project 1");
        Project project2 = new Project(counter.getAndIncrement(), "Project 2");
        Project project3 = new Project(counter.getAndIncrement(), "Project 3");

        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
    }

    @Operation(summary = "Get projects", description = "Retrieves the list of all available projects on the server")
    @ApiResponse(responseCode = "200", description = "Projects found and returned successfully")
    @GetMapping("/projects")
    public List<Project> getProjects() {
        return this.projects;
    }

    @Operation(summary = "Finds project by ID", description = "Returns a single project whose ID was given")
    @ApiResponse(responseCode = "200", description = "Project found successfully")
    @ApiResponse(responseCode = "404", description = "Project not found")
    @GetMapping("/projects/{id}")
    public Project getProject(@Parameter(description = "ID of the searched project") @PathVariable("id") long id) {

        for (Project project : this.projects) {
            if (project.getId() == id) {
                return project;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Project with ID %s not found!", id));
    }

    @Operation(summary = "Adds a new project", description = "The details of the project will be extracted from the request body ")
    @ApiResponse(responseCode = "201", description = "Project created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request body")
    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@Parameter(description = "From the automatically JSON parsed Request Body a project " +
            "will be created. The request Body contains all necessary attributes which are needed to successfully create a project ")                                     @Valid @RequestBody Project requestBody) {
        if (requestBody.getName()==null || requestBody.getName().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request body does not match the required one!");
        Project project = new Project(counter.getAndIncrement(), requestBody.getName());
        this.projects.add(project);
        return project;
    }


    @Operation(summary = "Updates project by ID", description = "modifies the data of the given project")
    @ApiResponse(responseCode = "200", description = "Project has been successfully updated")
    @ApiResponse(responseCode = "404", description = "Project not found")
    @PutMapping("/projects/{id}")
    public Project updateProject(@Parameter(description = "The id of the project to modify") @PathVariable("id") Integer id,
                                 @Parameter(description = "Contains the new name of the project") @Valid @RequestBody Project requestBody) {
        requestBody.setId(id);
        for (int i = 0; i < this.projects.size(); i++) {
            if (this.projects.get(i).getId() == (id)) {
                this.projects.set(i, requestBody);
                return requestBody;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Project with ID %s not found!", id));
    }

    @ApiResponse(responseCode = "200", description = "Project found and deleted successfully")
    @ApiResponse(responseCode = "404", description = "Project not found")
    @Operation(summary = "deletes project by ID", description = "Deletes the project from the server")
    @DeleteMapping("/projects/{id}")
    public Project deleteProject(@Parameter(description = "The id of the project to delete") @PathVariable("id") Integer id) {

        for (int x = 0; x < this.projects.size(); x++) {
            if (this.projects.get(x).getId() == id) {
                Project deletedProject = this.projects.get(x);
                this.projects.remove(x);
                return deletedProject;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Project with ID %s not found!", id));
    }
}
