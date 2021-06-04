package com.tingco.codechallenge.elevator.api.impl;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;

@Service
public class ElevatorControllerImpl implements ElevatorController {
    @Value("${com.tingco.elevator.startFloor}")
    private int startFoor;

    @Value("${com.tingco.elevator.endFloor}")
    private int endFloor;

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;


    private List<Elevator> elevators = new ArrayList<>();
    private Queue<Integer> waitingPersons = new LinkedList<>();
    @Autowired
    private Executor executor;



    @Override
    public Elevator requestElevator(int toFloor) {
        Elevator nearest =null;
        for(int i=0;i<numberOfElevators;i++){
            Elevator current = elevators.get(i);
            if(current.getState().equals(Elevator.State.IDLE)){
                nearest=current;
                if(Math.abs(nearest.currentFloor()-toFloor)>Math.abs(current.currentFloor()-toFloor)){
                    nearest =current;
                }
            }
        }
        if(nearest !=null) {
            nearest.setState(Elevator.State.OCCUPIED);
            System.out.println("Nearest Elevator is" + nearest.getId());
        }

        return nearest;
    }
    @PostConstruct
    public void initializeElevators(){
        for(int i=0;i<numberOfElevators;i++){
            ElevatorImpl elevator= new ElevatorImpl(i);
            executor.execute(elevator);
            elevators.add(elevator);
           System.out.println(elevators.size());
        }

    }
    @Override
    public List<Elevator> getElevators() {
        return elevators;
    }

    @Override
    public void releaseElevator(Elevator elevator) {

    }
}
