package com.company;

public class wagedata {
    public double code;
    public String description;

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public wagedata(double code, String description) {
        this.code = code;
        this.description = description;
    }
}
