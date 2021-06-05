package com.tingco.codechallenge.elevator.resources;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.api.util.ValidationUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiOperation(value = "Get All Elevators", notes = "This method returns all Elevators and their details")
    @RequestMapping(value = "/elevators", method = RequestMethod.GET)
    public List<Elevator> getElevators() {
            return controller.getElevators();
    }

    /**
     * Requests any elevator to arrive on the waiting floor(toFloor).
     * @param toFloor
     * @return
     */
    @ApiOperation(value = "Request Elevator", notes = "This method places request for elevator to given floor")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toFloor", value = "request an elevator to the floor (example - any number between 0 to 30)", required = true, dataType = "int", paramType = "query", defaultValue = "0")
    })
    @RequestMapping(value = "/elevator", method = RequestMethod.POST)
    public Object requestElevator(@RequestParam(required = true) Integer toFloor) {
        if(!util.validateFloor(toFloor)){
            logger.error("Input Floor is invalid");
            return ResponseEntity.notFound().build();
        }
        Elevator elevator = controller.requestElevator(toFloor);
        if(elevator==null){
            logger.info("No Idle Elevator found - Adding Request to Queue");
            return ResponseEntity.accepted().body(null);
        }
        return ResponseEntity.ok(elevator);
    }

    /**
     * This will release elevator and set state to IDLE
     * @param id
     * @return
     */
    @ApiOperation(value = "Release a elevator", notes = "This method will release elevator with given id.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "elevator id (example - any number between 0 to 5)", required = true, dataType = "int", paramType = "query", defaultValue = "0")
    })
    @RequestMapping(value = "/release", method = RequestMethod.POST)
    public ResponseEntity<String> releaseElevator(@RequestParam(required = true) Integer id) {
        if(!util.validateId(id)){
            logger.error("Elevator Id is invalid");
            return ResponseEntity.notFound().build();
        }
        List<Elevator> elevators = controller.getElevators();
        controller.releaseElevator(elevators.get(id));
        return ResponseEntity.ok("success");
    }

}
