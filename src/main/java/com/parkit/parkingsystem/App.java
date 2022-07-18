package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author o.froidefond
 * class principal.
 */
public class App {
    private static final Logger logger = LogManager.getLogger("App");

    /**
     * fonction principal.
     *
     * @param args non utilisé
     */
    public static void main(String[] args) {
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
