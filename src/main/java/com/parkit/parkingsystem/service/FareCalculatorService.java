package com.parkit.parkingsystem.service;




import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.util.Date;


public class FareCalculatorService {

    public void calculateFare(Ticket ticket, int lineCount){


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

        if (calFinal <= 0.5){
            calFinal = 0.0;
        }



        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (lineCount >= Fare.NUMBER_LINE){
                    ticket.setPrice((calFinal * Fare.CAR_RATE_PER_HOUR)-((calFinal*Fare.CAR_RATE_PER_HOUR)*0.05));
                }else{
                    ticket.setPrice(calFinal * Fare.CAR_RATE_PER_HOUR);
                }

                break;
            }
            case BIKE: {
                if (lineCount >= Fare.NUMBER_LINE) {
                    ticket.setPrice((calFinal * Fare.BIKE_RATE_PER_HOUR)-((calFinal*Fare.BIKE_RATE_PER_HOUR)*0.05));
                }else{
                    ticket.setPrice(calFinal * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}