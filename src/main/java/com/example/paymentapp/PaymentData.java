package com.example.paymentapp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class PaymentData  {
    private String NameAndSurname;
    private String AppointmentHour;
    private double Cash;
    private double Card;
    private DoubleProperty Total;
    private Date DateOfSave;

    public PaymentData(String name, String hour, double cash, double card) {
        NameAndSurname = name;
        AppointmentHour = hour;
        Cash = cash;
        Card = card;
        Total = new SimpleDoubleProperty();
        Total.set(Card+Cash);
        DateOfSave = new Date();
    }

    public String getNameAndSurname() {
        return NameAndSurname;
    }
    public String getAppointmentHour() {
        return AppointmentHour;
    }
    public double getCash() {
        return Cash;
    }
    public double getCard() {
        return Card;
    }
    public DoubleProperty getTotal() {
        return Total;
    }
    public Date getDateOfSave() {
        return DateOfSave;
    }

    public void setNameAndSurname(String nameAndSurname) {
        NameAndSurname = nameAndSurname;
    }
    public void setAppointmentHour(String appointmentHour) {
        AppointmentHour = appointmentHour;
    }
    public void setCash(double cash) {
        Cash = cash;
        Total.set(Card+Cash);
    }
    public void setCard(double card) {
        Card = card;
        Total.set(Card+Cash);
    }

    public String toString(){
        return AppointmentHour + ", " + NameAndSurname + ", " + Cash + ", " + Card + ", " + Total.doubleValue() ;
    }

}
