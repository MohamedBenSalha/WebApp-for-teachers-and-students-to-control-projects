package de.unistuttgart.iste.pe2ws20.api.team;

/**
 * class representing the format of the teams required by the HTTP Request (Get All Teams)
 *
 * @author Mohamed Ben Salha
 * @version 21.02.2021
 */
public class TeamReturn   {
    private int id;
    private String name;
    private int projectId;
    private String projectName;
    private int numberOfStudents;

    public TeamReturn(int id,String name, int projectId, String projectName, int numberOfStudents) {
        this.setNumberOfStudents(numberOfStudents);
        this.setProjectId(projectId);
        this.setProjectName(projectName);
        this.setName(name);
        this.setId(id);
    }


    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
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
}
