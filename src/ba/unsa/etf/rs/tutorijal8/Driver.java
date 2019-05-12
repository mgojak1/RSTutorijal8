package ba.unsa.etf.rs.tutorijal8;

import java.time.LocalDate;

public class Driver {
    private String name,surname,umcn;
    private LocalDate birthday,hireDate;
    private int id;

    public Driver(String name, String surname, String umcn, LocalDate birthday, LocalDate hireDate) {
        this.name = name;
        this.surname = surname;
        this.umcn = umcn;
        this.birthday = birthday;
        this.hireDate = hireDate;
        id = -1;
    }

    public Driver() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUmcn() {
        return umcn;
    }

    public void setUmcn(String umcn) {
        this.umcn = umcn;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Driver driver = (Driver) o;

        if (umcn != null ? !umcn.equals(driver.umcn) : driver.umcn != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (umcn != null ? umcn.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (hireDate != null ? hireDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name +" "+ surname +" ( "+ umcn +" )";
    }
}
