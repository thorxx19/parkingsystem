package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final Logger logger = LogManager.getLogger("TicketDAO");
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterAll
    private static void tearDown() {

    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    @DisplayName("test l'entrée du véhicule")
    public void testParkingACar() {
        try {
            ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.processIncomingVehicle();
            Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
            ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
            Assertions.assertNotEquals(null, ticket);
            Assertions.assertNotEquals(1, parkingSpot.getId());
        } catch (Exception e) {
            logger.error("Error fetching next available slot", e);
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("test l'entrée et la sortie du véhicule")
    public void testParkingLotExit() {
        try {
            ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.processIncomingVehicle();
            Thread.sleep(1000);
            parkingService.processExitingVehicle();
            Ticket ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
            Assertions.assertEquals(ticket.getPrice(), 0.0);
            Assertions.assertNotEquals(null, ticket.getOutTime());
        } catch (Exception e) {
            logger.error("Error fetching next available slot", e);
            e.printStackTrace();
        }
    }
}
