package com.tingco.codechallenge.elevator.api.util;

import com.tingco.codechallenge.elevator.api.impl.ElevatorControllerImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    private static final Logger logger = Logger.getLogger(ValidationUtil.class);

    @Value("${com.tingco.elevator.startFloor}")
    private int startFoor;

    @Value("${com.tingco.elevator.endFloor}")
    private int endFloor;

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    public boolean validateFloor(int toFloor){
        if (toFloor < startFoor || toFloor >endFloor) {
            logger.error("Not valid Floor: "+toFloor);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public boolean validateId(int id){
        if (id < 0 || id >=numberOfElevators) {
            logger.error("Not valid Elevator id : "+id);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
