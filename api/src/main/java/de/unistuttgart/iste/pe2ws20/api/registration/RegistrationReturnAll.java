package de.unistuttgart.iste.pe2ws20.api.registration;


import java.util.Date;
import java.util.List;

/**
 * This class was implemented only to make the response body of the HTTP requests (GET ALL REGISTRATIONS)
 * correspond to the tasks.
 *
 * @author Mohamed Ben Salha
 * @version 21.02.2021
 */
public class RegistrationReturnAll {

    private int id;
    private List<Integer> preferredProjectIds;
    private int numberOfStudents;
    private long created;


    public RegistrationReturnAll() {
    }

    public RegistrationReturnAll(final int id, final List<Integer> preferredProjectIds, final int studentsSize) {
        this.setCreated(new Date().getTime());
        this.setNumberOfStudents(studentsSize);
        this.setPreferredProjectIds(preferredProjectIds);
        this.setId(id);

    }


    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(final int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
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

    public void setPreferredProjectIds( final List<Integer> preferredProjectIds) {
        this.preferredProjectIds = preferredProjectIds;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated( final long created) {
        this.created = created;
    }
}