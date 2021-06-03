package com.tingco.codechallenge.elevator;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.impl.ElevatorControllerImpl;
import org.junit.Before;
import org.junit.Test;

public class ElevatorTest {
    ElevatorControllerImpl controller;
    @Before
    public void setUp(){
        controller = new ElevatorControllerImpl();

    }
   /* @Test
    public void testSetElevators(){
        controller.setElevators();
    }

    @Test
    public void testRequestElevator(){
        Elevator elevator = controller.requestElevator(5);
        System.out.println(elevator.getId());
    }*/

}
