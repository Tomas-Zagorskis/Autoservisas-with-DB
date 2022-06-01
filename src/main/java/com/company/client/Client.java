package com.company.client;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client")
public class Client {

    @Column(name = "auto")
    private String autoName;
    @Id
    @Column(name = "plate_nr")
    private String plateNr;
    private String name;
    private String surname;
    private String issue;
    private boolean fixed;
    private double costs;

    public Client() {
    }

    public Client(String name, String surname, String autoName, String plateNr, String issue) {

        this.name = name;
        this.surname = surname;
        this.autoName = autoName;
        this.plateNr = plateNr;
        this.issue = issue;
        this.fixed = false;
        this.costs = 0.0;
    }

    public Client(String name, String surname, String autoName, String plateNr, String issue, boolean fixed, double costs) {

        this.name = name;
        this.surname = surname;
        this.autoName = autoName;
        this.plateNr = plateNr;
        this.issue = issue;
        this.fixed = fixed;
        this.costs = costs;
    }

    public String getAutoName() {
        return autoName;
    }

    public void setAutoName(String autoName) {
        this.autoName = autoName;
    }

    public String getPlateNr() {
        return plateNr;
    }

    public void setPlateNr(String plateNr) {
        this.plateNr = plateNr;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    public String getName() {

        return name;
    }

    public String getSurname() {

        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    @Override
    public String toString() {

        return "Client{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", autoName='" + autoName + '\'' +
                ", plateNr='" + plateNr + '\'' +
                ", issue='" + issue + '\'' +
                ", fixed=" + fixed +
                ", costs=" + costs +
                '}';
    }
}
