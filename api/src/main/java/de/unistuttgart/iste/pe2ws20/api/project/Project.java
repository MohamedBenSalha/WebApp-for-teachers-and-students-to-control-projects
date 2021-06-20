package de.unistuttgart.iste.pe2ws20.api.project;

import de.unistuttgart.iste.pe2ws20.api.registration.Registration;
import de.unistuttgart.iste.pe2ws20.api.students.Student;

/**
 * Class representing projects which can be either added, edited or deleted from the course instructors
 * or chosen by students {@link Student} in their registrations {@link Registration}
 *
 * @author Mohamed Ben Salha
 * @version 12.02.2021
 */
public class Project {

    private int id;
    private String name;

    public Project(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public Project() {
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
