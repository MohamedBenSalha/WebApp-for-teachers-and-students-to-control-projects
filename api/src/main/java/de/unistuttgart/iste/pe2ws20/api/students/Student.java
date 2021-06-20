package de.unistuttgart.iste.pe2ws20.api.students;
/**
 * class representing a student object which has an id, name, email und ilias name.
 *
 * @author Mohamed Ben Salha
 * @version  12.02.2021
 */
public class Student {
    private String prename;
    private String name;
    private String email;
    private String iliasName;

    public Student() {
    }

    public Student(final String prename, final String name, final String email, final String iliasName) {

        this.setPrename(prename);
        this.setName(name);
        this.setEmail(email);
        this.setIliasName(iliasName);
    }






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIliasName() {
        return iliasName;
    }

    public void setIliasName(String iliasName) {
        this.iliasName = iliasName;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }
}
