package com.tingco.codechallenge.elevator.api.impl;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executor;


@Service
public class ElevatorControllerImpl implements ElevatorController {

    private static final Logger logger = Logger.getLogger(ElevatorControllerImpl.class);

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

    @Autowired
    private EventBus eventBus;


    @Override
    public Elevator requestElevator(int toFloor) {
        logger.info("Received Request for Floor: "+ toFloor);
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
            logger.info("Sending Elevator : "+ nearest.getId());
        }

        return nearest;
    }
    @PostConstruct
    public void initializeElevators(){
        for(int i=0;i<numberOfElevators;i++){
            ElevatorImpl elevator= new ElevatorImpl(i,eventBus);
            executor.execute(elevator);
            elevators.add(elevator);

        }

    }
    @Override
    public List<Elevator> getElevators() {
        logger.info("Returning List of Elevators");
        return Collections.unmodifiableList(elevators);
    }

    @Override
    public void releaseElevator(Elevator elevator) {
        elevator.setState(Elevator.State.IDLE);
        eventBus.post(elevator);
    }
}
