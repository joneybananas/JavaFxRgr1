package com.example.account;

public class Account {
    private String number;
    private double sum;
    private double weight;

    public Account (String number, double sum, double weight) {
        this.number = number;
        this.sum = sum;
        this.weight = weight;
    }

    public void setNumber (String number) {
        this.number = number;
    }

    public void setSum (double sum) {
        this.sum = sum;
    }

    public String getNumber () {
        return this.number;
    }

    public double getSum () {
        return this.sum;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString(){
        return number + " " + sum + " " + weight;
    }
}
