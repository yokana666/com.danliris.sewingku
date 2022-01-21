package com.danliris.sewingku.object;

public class Identity {

    String npk;
    String firstname;
    String lastname;
    String unit;


    public Identity(String npk, String firstname, String lastname, String unit) {
        this.npk = npk;
        this.firstname = firstname;
        this.lastname = lastname;
        this.unit = unit;
    }

    public String getNpk() {
        return npk;
    }

    public void setNpk(String npk) {
        this.npk = npk;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {return lastname; }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
