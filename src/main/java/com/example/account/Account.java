package com.example.account;

public class Account {
    private String number;
    private double sum;
    private boolean blocked;

    public Account (String number, double sum, boolean blocked) {
        this.number = number;
        this.sum = sum;
        this.blocked = blocked;
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

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString(){
        return number + " " + sum + " " + blocked;
    }
}
