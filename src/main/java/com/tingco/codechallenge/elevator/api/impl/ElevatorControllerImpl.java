package com.tingco.codechallenge.elevator.api.impl;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.api.beans.ElevatorEvent;
import com.tingco.codechallenge.elevator.api.beans.EventType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executor;

@EnableScheduling
@Service
public class ElevatorControllerImpl implements ElevatorController {

    private static final Logger logger = Logger.getLogger(ElevatorControllerImpl.class);

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    private List<Elevator> elevators = new ArrayList<>();

    private Queue<Integer> waitingPersons = new LinkedList<>();

    @Autowired
    private Executor executor;

    @Autowired
    private EventBus eventBus;


    /**
     * Initialize all elevators
     */
    @PostConstruct
    public void initializeElevators(){
        for(int i=0;i<numberOfElevators;i++){
            ElevatorImpl elevator= new ElevatorImpl(i,eventBus);
            executor.execute(elevator);
            elevators.add(elevator);

        }
    }


    @Override
    public synchronized Elevator requestElevator(int toFloor) {
        logger.info("Received Request for Floor: "+ toFloor);
        Elevator nearest =null;
        for(Elevator current : elevators){
            if(!current.isBusy()){
                if(nearest == null){
                    nearest = current;
                    continue;
                }
                int nearestDistance = Math.abs(nearest.getCurrentFloor()-toFloor);
                int currentDistance = Math.abs(current.getCurrentFloor()-toFloor);
                if(nearestDistance > currentDistance){
                    nearest =current;
                }
            }
        }
        if(nearest != null) {
            logger.info("Sending Elevator : "+ nearest.getId());
            ElevatorEvent event = new ElevatorEvent(nearest.getId(),toFloor, EventType.ASSIGN);
            eventBus.post(event);
        } else {
            if(!waitingPersons.contains(toFloor))
                logger.info("ADD TO WAITING QUEUE - floor : "+ toFloor);
                waitingPersons.add(toFloor);
        }
        return nearest;
    }

    public void takeElevator(int id, int destFloor){
       //TODO: Take given elevator id to destination floor
    }

    /**
     * Scheduled Method to request Elevator for persons waiting in Queue
     */
    @Scheduled(fixedRate = 1000)
    public void getFromQueue(){
        if(!waitingPersons.isEmpty()){
           int reqFloor= waitingPersons.peek();
           logger.info("Requesting for waiting person at floor : "+ reqFloor);
           Elevator elevator = requestElevator(reqFloor);
           if(elevator!=null){
               waitingPersons.poll();
           }
        }
    }



    @Override
    public List<Elevator> getElevators() {
        logger.info("Returning List of Elevators");
        return Collections.unmodifiableList(elevators);
    }


    @Override
    public synchronized void releaseElevator(Elevator elevator) {
        logger.info("In releaseElevator");
        ElevatorEvent event = new ElevatorEvent(elevator.getId(), EventType.RELEASE);
        eventBus.post(event);
    }


}
