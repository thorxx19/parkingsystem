package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Date in = ticket.getInTime();
        Date out = ticket.getOutTime();
        long diff = Math.abs(out.getTime() - in.getTime())/1000;
        double calculeHeure = Math.floorDiv(diff,3600);
        double calculeMinute = Math.floorDiv(diff,60) - calculeHeure * 60;
        double cal = calculeMinute/60;
        double calFinal = calculeHeure + cal ;


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(calFinal * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(calFinal * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}