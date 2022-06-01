package com.company;

import javax.persistence.*;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private double expenses;
    private double income;

    public Budget() {}

    public Budget(double expenses, double income) {

        this.expenses = expenses;
        this.income = income;
    }

    public void addIncome(double income) {

        this.income += income;
    }

    public void addExpenses(double expenses) {

        this.expenses += expenses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
