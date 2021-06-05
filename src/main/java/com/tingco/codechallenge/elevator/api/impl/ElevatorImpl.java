package com.tingco.codechallenge.elevator.api.impl;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.beans.ElevatorEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ElevatorImpl implements Elevator, Runnable {

    private static final Logger logger = Logger.getLogger(ElevatorImpl.class);
    private int id;
    private int currFloor;
    private int startFloor;
    private int endFloor;
    private Direction direction;
    private State state;
    private int destFloor;

    @Autowired
    private EventBus eventBus;

    public ElevatorImpl() {
    }

    public ElevatorImpl(int elevID, EventBus eventBus) {
        this.id = elevID;
        this.direction = Direction.NONE;
        this.state = State.IDLE;
        this.eventBus=eventBus;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State newState) {
        this.state = newState;
    }

    @Override
    public Direction getDirection() {

        return direction;
    }

    @Override
    public int getAddressedFloor() {
        return currFloor;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void moveElevator(int toFloor) {
        if (currFloor < toFloor) {
            this.direction = Direction.UP;
            this.state = State.MOVING_UP;
            while(currFloor < toFloor){
                logger.info(String.format("Elevator [%s]  going up to floor [%s] from [%s] ", id, toFloor, currFloor));
                currFloor++;
            }
        } else {
            this.direction = Direction.DOWN;
            this.state = State.MOVING_DOWN;
            while(currFloor < toFloor) {
                logger.info(String.format("Elevator [%s]  going down to floor [%s] from [%s] ", id, toFloor, currFloor));
                currFloor--;
            }
        }

        logger.info(String.format("Elevator [%s] has reached destination [%s] ", id, currFloor));

    }

    @Override
    public boolean isBusy() {
        if (!this.state.equals(State.IDLE)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int currentFloor() {
        return currFloor;
    }

    @Override
    public void run() {
        logger.info(String.format("Elevator [%s] is available on floor [%s] ", id, currFloor));
    }


    @Subscribe
    public void getEvent(Elevator elevator){
        System.out.println("Got Event");
        if(elevator.getId()==this.getId()){
            this.setState(elevator.getState());
        }
    }

}
