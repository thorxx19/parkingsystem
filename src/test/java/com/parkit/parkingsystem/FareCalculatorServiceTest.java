package com.parkit.parkingsystem;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static com.parkit.parkingsystem.constants.Fare.*;
import java.util.Date;

@Tag("test-unitaire-fare_calculator")
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    @DisplayName("ticket pour une heure de stationnement pour une voiture")
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,0);

        assertEquals(ticket.getPrice(), CAR_RATE_PER_HOUR);
    }

    @Test
    @DisplayName("ticket pour une heure de stationnement pour une moto")
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,0);

        assertEquals(ticket.getPrice(), BIKE_RATE_PER_HOUR);
    }

    @Test
    @DisplayName("vérifie que l'exeption est levée quand il manque le type de véhicule")
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket,0));
    }
    @Test
    @DisplayName("vérifie que l'exception est lévée quand le type de véhicule correspond a aucun connu")
    public void calculateFareTruckType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.TRUCK,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,0));
    }

    @Test
    @DisplayName("vérifie que la date de sortie ne soit pas antérieure à celle de l'entrée")
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,0));
    }

    @Test
    @DisplayName("calcule le ticket pour une moto stationnée 45 minutes")
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,0);

        assertEquals((0.75 * BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }
    @Test
    @DisplayName("calcule le ticket pour une voiture stationnée 45 minutes")
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,0);

        assertEquals( (0.75 * CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    @Test
    @DisplayName("ticket pour une moto avec 25 minutes de stationnement")
    public void calculateFareBikeWithLessThanTwentyFiveParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  25 * 60 * 1000) );//25 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,0);

        assertEquals((0.0), ticket.getPrice() );
    }
   @Test
   @DisplayName("ticket pour une voiture avec 25 minutes de stationnement")
   public void calculateFareCarWithLessThanTwentyFiveParkingTime(){
       Date inTime = new Date();
       inTime.setTime( System.currentTimeMillis() - (  25 * 60 * 1000) );//25 minutes parking time should give 3/4th parking fare
       Date outTime = new Date();
       ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

       ticket.setInTime(inTime);
       ticket.setOutTime(outTime);
       ticket.setParkingSpot(parkingSpot);
       fareCalculatorService.calculateFare(ticket,0);

       assertEquals((0.0), ticket.getPrice() );
   }
    @Test
    @DisplayName("calcule le ticket pour une voiture stationnée une journée")
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,0);

        assertEquals( (24 * CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    @Test
    @DisplayName("calcule le ticket pour une moto stationnée une journée")
    public void calculateFareBikeWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,0);

        assertEquals( (24 * BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }
    @Test
    @DisplayName("calcule un ticket pour une voiture stationnée 45 minutes avec la reduction de 5%")
    public void calculateFareCarWithFivePourcentReduc(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,5);

        assertEquals( ((0.75 * CAR_RATE_PER_HOUR)-(0.75 * CAR_RATE_PER_HOUR)*REDUCTION) , ticket.getPrice());
    }
    @Test
    @DisplayName("calcule un ticket pour une moto stationnée 45 minute avec la reduction de 5%")
    public void calculateFareBikeWithFivePourcentReduc(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket,5);

        assertEquals(((0.75 * BIKE_RATE_PER_HOUR)-(0.75 * BIKE_RATE_PER_HOUR)*REDUCTION), ticket.getPrice());
    }
}
