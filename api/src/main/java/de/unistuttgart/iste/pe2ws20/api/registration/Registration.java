package de.unistuttgart.iste.pe2ws20.api.registration;

import de.unistuttgart.iste.pe2ws20.api.project.Project;
import de.unistuttgart.iste.pe2ws20.api.students.Student;

import java.util.*;

/**
 * class representing a registration object which consists of an id, a list of the preferred projects chosen by the
 * students, the list of these students and the creation date.
 *
 * @author Mohamed Ben Salha
 * @version 21.02.2021
 */
public class Registration {
    private int id;
    private List<Project> preferredProjects;
    private List<Student> students;
    private Date created;

    public Registration() {
    }

    public Registration(final List<Project> preferredProjects, final List<Student> students) {
        this.setPreferredProjects(preferredProjects);
        this.setStudents(students);
        created = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Project> getPreferredProjects() {
        return preferredProjects;
    }

    public void setPreferredProjects(List<Project> preferredProjects) {
        this.preferredProjects = preferredProjects;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
