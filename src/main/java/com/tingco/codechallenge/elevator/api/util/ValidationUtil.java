package com.tingco.codechallenge.elevator.api.util;

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

    /**
     * To Check floor is in range of Start and End Floor
     * @param toFloor
     * @return
     */
    public boolean validateFloor(int toFloor){
        if (toFloor < startFoor || toFloor >endFloor) {
            logger.error("Not valid Floor: "+toFloor);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * To Check if it is a valid elevator id.
     * @param id
     * @return
     */
    public boolean validateId(int id){
        if (id < 0 || id >=numberOfElevators) {
            logger.error("Not valid Elevator id : "+id);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
