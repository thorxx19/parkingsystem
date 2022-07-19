package com.parkit.parkingsystem.service;

import static com.parkit.parkingsystem.constants.Fare.*;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

/**
 * @author o.froidefond
 */
public class FareCalculatorService {

    /**
     * fonction pour calculer le prix du ticket
     *
     * @param ticket    un ticket avec ses infos
     * @param lineCount Nombre de tickets pour ce véhicule
     */
    public void calculateFare(Ticket ticket, int lineCount) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
        Date in = ticket.getInTime();
        Date out = ticket.getOutTime();
        long diff = Math.abs(out.getTime() - in.getTime()) / 1000;
        double calculeHeure = Math.floorDiv(diff, 3600);
        double calculeMinute = Math.floorDiv(diff, 60) - calculeHeure * 60;
        double cal = calculeMinute / 60;
        double calFinal = calculeHeure + cal;

        if (calFinal <= FREE_TIME) {
            calFinal = 0.0;
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (lineCount >= NUMBER_LINE) {
                    ticket.setPrice((calFinal * CAR_RATE_PER_HOUR) - ((calFinal * CAR_RATE_PER_HOUR) * REDUCTION));
                } else {
                    ticket.setPrice(calFinal * CAR_RATE_PER_HOUR);
                }
                break;
            }
            case BIKE: {
                if (lineCount >= NUMBER_LINE) {
                    ticket.setPrice((calFinal * BIKE_RATE_PER_HOUR) - ((calFinal * BIKE_RATE_PER_HOUR) * REDUCTION));
                } else {
                    ticket.setPrice(calFinal * BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}