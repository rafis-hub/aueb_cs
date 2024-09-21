package com.company;
import java.util.*;

public class ergazomenoi {
    private String name;
    private String full_name;
    private double wage;
    private double pae;    /* pae = pososto asfalistikwn eisforwn */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public double getPae() {
        return pae;
    }

    public void setPae(double pae) {
        this.pae = pae;
    }

    public  ergazomenoi(String name, String full_name, double wage, double pae ) {
        this.name = name;
        this.full_name = full_name;
        this.wage = wage;
        this.pae = pae;

    }

    public ergazomenoi(){}

    @Override
    public String toString() {
        return "ergazomenoi{" +
                "name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", wage=" + wage +
                ", pae=" + pae +
                '}';
    }
}
