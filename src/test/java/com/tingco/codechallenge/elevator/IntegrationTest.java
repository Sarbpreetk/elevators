package com.tingco.codechallenge.elevator;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.config.ElevatorApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@SpringApplicationConfiguration(classes = ElevatorApplication.class)
public class IntegrationTest {

    @Autowired
    ElevatorController controller;

    @Test
    public void simulateAnElevatorShaft() {

        List<Elevator> elevators = controller.getElevators();

        Assert.assertEquals(6, elevators.size());

        Elevator elevator1 =  controller.requestElevator(11);
        Assert.assertNotNull(elevator1);

        Elevator elevator2 = controller.requestElevator(8);
        Assert.assertNotNull(elevator2);

        Elevator elevator3 =  controller.requestElevator(6);
        Assert.assertNotNull(elevator3);

        Elevator elevator4 =  controller.requestElevator(7);
        Assert.assertNotNull(elevator4);

        Elevator elevator5 =  controller.requestElevator(3);
        Assert.assertNotNull(elevator5);


        Elevator elevator6 =  controller.requestElevator(14);
        Assert.assertNotNull(elevator6);


        //Seventh call shall return null as all the Elevators are occupied
        //This can be tested standalone not with other test cases
        //Elevator elevator7 =  controller.requestElevator(8);
        // Assert.assertNull(elevator7);

        // Delay for 10 seconds for first 2 elevators to reach
        delay(20);

        int nearestElevatorId = elevator6.getId();
        Elevator elevator8 =  controller.requestElevator(20);
        Assert.assertNotNull(elevator8);
        Assert.assertEquals(nearestElevatorId, elevator8.getId());
    }


    private  void delay(int timeInSeconds){
        try {
            Thread.sleep(timeInSeconds * 1000 );
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRequestElevator(){
        List<Elevator> elevators = controller.getElevators();
        Elevator elevator1 = controller.requestElevator(5);
        delay(2);
        Assert.assertNotNull(elevator1);
        Assert.assertEquals(5, elevator1.getAddressedFloor());
        Assert.assertEquals(Elevator.Direction.UP, elevator1.getDirection());
        Assert.assertTrue(elevator1.isBusy());
        Assert.assertEquals(Elevator.State.MOVING_UP, elevator1.getState());
        delay(10);
        Assert.assertEquals(elevator1.getAddressedFloor(), elevator1.getCurrentFloor());
        Assert.assertEquals(Elevator.State.IDLE, elevator1.getState());

    }



}
