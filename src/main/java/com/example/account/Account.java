package com.example.account;

public class Account {
    private String number, sum;

    public Account () {
        this.number = "1";
        this.sum = "1";
    }

    public Account (String number, String sum) {
        this.number = number;
        this.sum = number;
    }

    public void setNumber (String number) {
        this.number = number;
    }

    public void setSum (String sum) {
        this.sum = sum;
    }

    public String getNumber () {
        return this.number;
    }

    public String getSum () {
        return this.sum;
    }
}
