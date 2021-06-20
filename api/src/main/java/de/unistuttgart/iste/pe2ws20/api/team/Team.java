package de.unistuttgart.iste.pe2ws20.api.team;

import de.unistuttgart.iste.pe2ws20.api.students.Student;

import java.util.List;

/**
 * class representing a team consisting of an id, a name, a projectID, a project name
 * and a list of students.
 *
 * @author Mohamed Ben Salha
 * @version 21.02.2021
 */
public class Team {
    private int id;
    private String name;
    private int projectId;
    private String projectName;
    private List<Student> students;

    public Team(int id, String name, int projectId, String projectName, List<Student> students) {
        this.setId(id);
        this.setStudents(students);
        this.setProjectId(projectId);
        this.setProjectName(projectName);
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
