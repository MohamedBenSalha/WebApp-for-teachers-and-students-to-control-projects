package de.unistuttgart.iste.pe2ws20.api.registration;

import de.unistuttgart.iste.pe2ws20.api.students.Student;

import java.util.List;

/**
 * This class was implemented only to make the response body of the HTTP requests (GET ONE SPECIFIC REGISTRATION)
 * correspond to the tasks.
 *
 * @author Mohamed Ben Salha
 * @version 21.02.2021
 */
public class RegistrationReturnByID {

    private int id;
    private List<Integer> preferredProjectIds;
    private List<Student> students;
    private long created;

    public RegistrationReturnByID() {
    }

    public RegistrationReturnByID(final int id, final List<Integer> preferredProjectIds,
                                  final List<Student> students, final long created) {
        this.setId(id);
        this.setPreferredProjectIds(preferredProjectIds);
        this.setCreated(created);
        this.setStudents(students);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getPreferredProjectIds() {
        return preferredProjectIds;
    }

    public void setPreferredProjectIds(List<Integer> preferredProjectIds) {
        this.preferredProjectIds = preferredProjectIds;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
