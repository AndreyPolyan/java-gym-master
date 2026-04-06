package ru.yandex.practicum.gym;

import java.util.Objects;

public class Coach implements Comparable<Coach> {

    //фамилия
    private String surname;
    //имя
    private String name;
    //отчество
    private String middleName;

    public Coach(String surname, String name, String middleName) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
    }

    @Override
    public int compareTo(Coach other) {
        int surnameCompare = surname.compareTo(other.surname);
        if (surnameCompare != 0) {
            return surnameCompare;
        }

        int nameCompare = name.compareTo(other.name);
        if (nameCompare != 0) {
            return nameCompare;
        }

        return middleName.compareTo(other.middleName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(surname, coach.surname)
                && Objects.equals(name, coach.name)
                && Objects.equals(middleName, coach.middleName);
    }

    @Override
    public String toString() {
        String result = this.surname + " " + this.name.charAt(0) + ".";

        if (middleName != null && !middleName.isEmpty()) {
            result += middleName.charAt(0) + ".";
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, middleName);
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }
}

