package by.pvt.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "pet")
public class Pet implements Serializable {


    private static final long serialVersionUID = 1L;


    @Id
    @GenericGenerator(name = "id", strategy = "increment")
    private int id;

    @Column
    private String name;

    @Column
    private String animal;

    @Column
    private Date dateOfBirth;

    @Column
    private boolean vaccination;

    @Column
    private char gender;

    public Pet() {
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

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isVaccination() {
        return vaccination;
    }

    public void setVaccination(boolean vaccination) {
        this.vaccination = vaccination;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;

        if (id != pet.id) return false;
        if (vaccination != pet.vaccination) return false;
        if (gender != pet.gender) return false;
        if (name != null ? !name.equals(pet.name) : pet.name != null) return false;
        if (animal != null ? !animal.equals(pet.animal) : pet.animal != null) return false;
        return dateOfBirth != null ? dateOfBirth.equals(pet.dateOfBirth) : pet.dateOfBirth == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (animal != null ? animal.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (vaccination ? 1 : 0);
        result = 31 * result + (int) gender;
        return result;
    }
}
