package de.unistuttgart.iste.pe2ws20.api.Logic;

import de.unistuttgart.iste.pe2ws20.api.project.Project;
import de.unistuttgart.iste.pe2ws20.api.registration.Registration;
import de.unistuttgart.iste.pe2ws20.api.students.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * class containing all logic methods needed by the controller to validate the request bodies.
 *
 * @author Mohamed Ben Salha
 * @version 12.02.2021
 */
public class Logic {
    /**
     * using the RESTapi, a get request is sent to retrieve all registrations available in the server.
     *
     * @return arraylist containing all registrations in the server
     */
    public ArrayList<Registration> getAllRegistrations() {
        RestTemplate restTemplate = new RestTemplate();
        String urlRegistrations = "http://localhost:8080/api/v1/submittedRegistrations";
        ResponseEntity<Registration[]> responseEntity = restTemplate.getForEntity(urlRegistrations, Registration[].class);
        Registration[] registrations = responseEntity.getBody();
        assert registrations != null;
        return new ArrayList<>(Arrays.asList(registrations));
    }

    /**
     * using the RESTapi, a get request is sent to retrieve all projects available in the server.
     *
     * @return arraylist containing all projects in the server
     */
    public ArrayList<Project> getAllProjects() {

        RestTemplate restTemplate = new RestTemplate();
        String urlProjects = "http://localhost:8080/api/v1/projects";
        ResponseEntity<Project[]> responseEntity1 = restTemplate.getForEntity(urlProjects, Project[].class);
        Project[] projects = responseEntity1.getBody();
        return new ArrayList<>(Arrays.asList(projects));
    }

    /**
     * chects whether the projects are valid. This is only true if the passed list contains no
     * duplicates and also contains the IDs of all existing projects on the server.
     *
     * @param projects chosen by the students
     * @return whether the projects are valid
     * @throws IllegalArgumentException when the projects are null
     */
    public boolean checkProjectsValidity(List<Project> projects) {
        if (projects == null)
            throw new IllegalArgumentException();
        // gets all IDs of the projects
        List<Integer> projectIDs = projects.stream().map(Project::getId).collect(Collectors.toList());
        // filters the ids and get a list without duplicates
        List<Integer> listWithoutDuplicates = new ArrayList<>(
                new HashSet<>(projectIDs));

        return listWithoutDuplicates.equals(getAllProjects().stream().map(Project::getId).collect(Collectors.toList()));
    }

    /**
     * one student is valid when his prename and his name are not empty, when the first letter of his Ilias Name
     * in uppercase is, when his ilias name does not contains spaces. In addition, the email address and ilias name
     * must be unique, i.e. they do not appear for any other student. Furthermore, the email address must end with
     * "@stud.uni-stuttgart.de".
     * If all these conditions are fulfilled then the student is considered valid.
     *
     * @param students occurring in the current registration
     * @return whether the students are valid
     */
    public boolean checkStudentsValidity(List<Student> students) {
        if (students.isEmpty()) return false;
        // create a list without duplicates from the ILIAS NAMES
        final List<String> iliasOfStudentsWithoutDuplicates = new ArrayList<>(
                new HashSet<>(students.stream().map(Student::getIliasName).collect(Collectors.toList())));
        // create a list without duplicates from the EMAIL ADDRESSES
        final List<String> emailsOfStudentsWithoutDuplicates = new ArrayList<>(
                new HashSet<>(students.stream().map(Student::getEmail).collect(Collectors.toList())));
        // check if there were duplicates
        if (emailsOfStudentsWithoutDuplicates.size() != students.size() || iliasOfStudentsWithoutDuplicates.size() != students.size())
            return false;

        List<Registration> allRegistrations = this.getAllRegistrations();
        List<String> allEmails = getAllExistingEmailAddresses(allRegistrations);
        List<String> allIliasNames = getAllExistingIliasNames(allRegistrations);

        for (Student student : students) {
            if (student.getPrename().equals("") || student.getIliasName().isEmpty() || student.getName().isEmpty()
                    || !student.getEmail().endsWith("@stud.uni-stuttgart.de") || student.getEmail().startsWith("@") || student.getIliasName().contains(" ") ||
                    !Character.isUpperCase(student.getIliasName().charAt(0)) || allEmails.contains(student.getEmail()) ||
                    allIliasNames.contains(student.getIliasName())) {
                return false;
            }
        }

        return true;

    }

    /**
     * maps the registrations to the email addresses of all occurring students
     *
     * @param allRegistrations existing on the server
     * @return list containing all email addresses of the students
     */
    private List<String> getAllExistingEmailAddresses(List<Registration> allRegistrations) {
        assert allRegistrations != null;
        final List<String> allEmails = new ArrayList<>();
        for (Registration registration : allRegistrations) {
            allEmails.addAll(registration.getStudents().stream().map(Student::getEmail).collect(Collectors.toList()));
        }
        return allEmails;
    }

    /**
     * maps the registrations to the Ilias Names of all occurring students
     *
     * @param allRegistrations existing on the server
     * @return list containing all Ilias Names of the students
     */
    private List<String> getAllExistingIliasNames(List<Registration> allRegistrations) {
        assert allRegistrations != null;
        final List<String> allIliasNames = new ArrayList<>();
        for (Registration registration : allRegistrations) {
            allIliasNames.addAll(registration.getStudents().stream().map(Student::getIliasName).collect(Collectors.toList()));
        }
        return allIliasNames;
    }


    /**
     * adds the students from the registration to one list
     *
     * @param registration Request Body containing the students
     * @return a list containing all students belonging to this registration
     */
    public ArrayList<Student> getStudents(Map<String, Object> registration) {
        ArrayList<Student> students = new ArrayList<>();
        ArrayList listOfStudents = (ArrayList) registration.get("students");
        for (Object student : listOfStudents) {
            Map<String, Object> studentMap = (Map<String, Object>) student;
            Student student1 = new Student((String) studentMap.get("prename"),
                    (String) studentMap.get("name"), (String) studentMap.get("email"), (String) studentMap.get(
                    "iliasName"));
            students.add(student1);
        }
        return students;

    }


    /**
     * gets the IDs of the projects from the given parameter. Searches all available Projects on the server
     * and then adds the projects whose ID matches the IDs in the registration to a list which wil be returned
     *
     * @param registration Request Body containing the IDs of the projects
     * @return a list containing all projects whose ID are present in the given registration
     */
    public ArrayList<Project> getPreferredProjects(Map<String, Object> registration) {
        ArrayList<Project> preferredProjects = new ArrayList<>();
        ArrayList<Project> allProjectsOnTheServer = getAllProjects();
        ArrayList<Integer> projects = (ArrayList<Integer>) registration.get("preferredProjectIds");
        // adds the projects to the list of preferred projects.
        for (final Integer projectId : projects) {
            for (final Project project : allProjectsOnTheServer) {
                if (project.getId() == projectId) {
                    preferredProjects.add(project);
                }
            }
        }
        return preferredProjects;
    }

}
