package com.tingco.codechallenge.elevator.resources;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
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
    @Autowired
    ElevatorController controller;

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
        return controller.getElevators();
    }

    @RequestMapping(value = "/elevatorTo/{toFloor}", method = RequestMethod.POST)
    public ResponseEntity<Elevator> requestElevator(@RequestParam int toFloor) {
        return ResponseEntity.ok(controller.requestElevator(toFloor));
    }

}
