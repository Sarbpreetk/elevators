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
    private static final int MOVE_TIME = 1000;
    private int id;
    private int currentFloor;
    private Direction direction;
    private State state;
    private int addressedFloor;


    @Autowired
    private EventBus eventBus;

    public ElevatorImpl() {
    }

    public ElevatorImpl(int elevID, EventBus eventBus) {
        this.id = elevID;
        this.direction = Direction.NONE;
        this.state = State.IDLE;
        this.eventBus = eventBus;
    }

    @Override
    public State getState() {
        return state;
    }


    @Override
    public Direction getDirection() {
        if (currentFloor == addressedFloor) return Direction.NONE;
        direction = currentFloor < addressedFloor ? Direction.UP : Direction.DOWN;
        return direction;
    }

    @Override
    public int getAddressedFloor() {
        return addressedFloor;
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void moveElevator(int toFloor) {
        if (currentFloor < toFloor) {
            this.direction = Direction.UP;
            this.state = State.MOVING_UP;
            while (currentFloor < toFloor) {
                logger.info(String.format("Elevator [%s]  going up to floor [%s] from [%s] ", id, toFloor, currentFloor));
                delay();
                currentFloor++;
            }
        } else {
            this.direction = Direction.DOWN;
            this.state = State.MOVING_DOWN;
            while (currentFloor > toFloor) {
                logger.info(String.format("Elevator [%s]  going down to floor [%s] from [%s] ", id, toFloor, currentFloor));
                delay();
                currentFloor--;
            }
        }

        logger.info(String.format("Elevator [%s] has reached destination [%s] ", id, currentFloor));
        this.direction = Direction.NONE;
        this.state = State.IDLE;

    }

    /**
     * Timed Delay - Elevator Moving time
     */
    private void delay() {
        try {
            Thread.sleep(MOVE_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    public void run() {
        logger.info(String.format("Elevator [%s] is available on floor [%s] ", id, currentFloor));
        eventBus.register(this);
    }

    /**
     * Subscriber method for Elevator event
     *
     * @param event
     */
    @Subscribe
    public void onEvent(ElevatorEvent event) {
        logger.debug(String.format("Elevator ID [%s] event for ID [%s] ", id, event.getElevatorId()));
        if (this.id == event.getElevatorId()) {
            switch (event.getEventType()) {
                case RELEASE:
                    this.state = State.IDLE;
                    this.direction = Direction.NONE;
                    break;
                case ASSIGN:
                    this.state = State.OCCUPIED;
                    this.addressedFloor = event.getDestFloor();
                    this.moveElevator(addressedFloor);
                    break;
                default:
                    logger.info("Not Supported Event");
            }
        }

    }

}
