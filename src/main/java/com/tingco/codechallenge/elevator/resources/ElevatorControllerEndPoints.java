package com.tingco.codechallenge.elevator.resources;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.api.util.ValidationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {

    private static final Logger logger = Logger.getLogger(ElevatorControllerEndPoints.class);

    @Autowired
    ElevatorController controller;
    @Autowired
    ValidationUtil util;

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {

        return "pong";
    }

    @RequestMapping(value = "/elevators", method = RequestMethod.GET)
    public List<Elevator> getElevators() {
        logger.info("In getElevators");
            return controller.getElevators();
    }

    @RequestMapping(value = "/elevator", method = RequestMethod.GET)
    public Object requestElevator(@RequestParam(required = true) Integer toFloor) {
        if(!util.validateFloor(toFloor)){
            return ResponseEntity.badRequest().build();
        }
        Elevator elevator = controller.requestElevator(toFloor);
        if(elevator==null){
            return ResponseEntity.accepted().body(null);
        }
        return ResponseEntity.ok(elevator);
    }


    @RequestMapping(value = "/release", method = RequestMethod.GET)
    public ResponseEntity<String> releaseElevator(@RequestParam(required = true) Integer id) {
        System.out.println("releaseElevator: "+id);
        if(!util.validateId(id)){
            return ResponseEntity.notFound().build();
        }
        List<Elevator> elevators = controller.getElevators();
        controller.releaseElevator(elevators.get(id));
        return ResponseEntity.ok("success");
    }

}
