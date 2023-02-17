package com.example.homepwner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Home {
    private UUID mId;
    private String name;
    private Date dateCreated;
    private String  serialNumber;
    private double valueInDollars;
    public Home() {
        mId = UUID.randomUUID();
        dateCreated = new Date();
    }
    public UUID getId() {
        return mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serialNumber;
    }


    public void setSerial() {
        String letNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        Random rnd = new Random();
        while (str.length() < 8) {
            int index = (int) (rnd.nextFloat() * letNum.length());
            str.append(letNum.charAt(index));
        }
        this.serialNumber = str.toString();

    }

    public Date getDate() {
        return dateCreated;
    }

    public void setDate(Date date) {
        this.dateCreated = date;
    }

    public double getValue() {
        return valueInDollars;
    }

    public void setValue(double value) {
        this.valueInDollars = value;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

}
