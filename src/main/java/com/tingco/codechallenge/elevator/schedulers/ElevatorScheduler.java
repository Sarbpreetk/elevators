package com.tingco.codechallenge.elevator.schedulers;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.api.beans.ElevatorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
@Component
@EnableScheduling
public class ElevatorScheduler {
    @Autowired
    ElevatorController controller;

    private Queue<ElevatorRequest> waitingPersons = new LinkedList<>();

    Scanner scan = new Scanner(System.in);

    public void useElevator(){
        ElevatorRequest request = new ElevatorRequest();
        System.out.println("Enter current floor");
        int reqFloor = scan.nextInt();
        request.setCurrFloor(reqFloor);
        System.out.println("Enter destination floor");
        int destnFloor = scan.nextInt();
        request.setDestFloor(destnFloor);
        Elevator elevator = controller.requestElevator(destnFloor);
        if(elevator==null){
            waitingPersons.add(request);
        }
        System.out.println("Waiting "+waitingPersons.size());
       if (!waitingPersons.isEmpty()){

       }
    }


}
