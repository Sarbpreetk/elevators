package com.tingco.codechallenge.elevator.api.impl;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.api.beans.ElevatorEvent;
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
    public synchronized Elevator requestElevator(int toFloor) {
        logger.info("Received Request for Floor: "+ toFloor);
        Elevator nearest =null;
        for(int i=0;i<numberOfElevators;i++){
            Elevator current = elevators.get(i);
            if(current.getState().equals(Elevator.State.IDLE)){
                if(nearest==null){
                    nearest =current;
                    continue;
                }
                if(Math.abs(nearest.currentFloor()-toFloor)>Math.abs(current.currentFloor()-toFloor)){
                    nearest =current;
                }
            }
        }
        if(nearest !=null) {
            logger.info("Sending Elevator : "+ nearest.getId());
            nearest.setState(Elevator.State.OCCUPIED);
            nearest.moveElevator(toFloor);
        } else {
            waitingPersons.add(toFloor);
        }

        return nearest;
    }

    @Scheduled(fixedRate = 1000)
    public void getFromQueue(){
        if(!waitingPersons.isEmpty()){
           int reqFloor= waitingPersons.poll();
            logger.info("Requesting for waiting person: "+ reqFloor);
           requestElevator(reqFloor);
        }
    }


    @PostConstruct
    public void initializeElevators(){
        for(int i=1;i<=numberOfElevators;i++){
            ElevatorImpl elevator= new ElevatorImpl(i,eventBus);
            eventBus.register(elevator);
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
    public synchronized void releaseElevator(Elevator elevator) {
        logger.info("in releaseElevator");
        elevator.setState(Elevator.State.IDLE);
        eventBus.post(elevator);
        System.out.println("Posted Event");
    }

    @Subscribe
    public void receiveEvent(Elevator elevator){
        System.out.println("received Event");
    }
}
