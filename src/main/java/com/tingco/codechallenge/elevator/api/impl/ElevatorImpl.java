package com.tingco.codechallenge.elevator.api.impl;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tingco.codechallenge.elevator.api.Elevator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElevatorImpl implements Elevator, Runnable {

    private int id;
    private int currFloor;
    private int startFloor;
    private int endFloor;
    private Direction direction;
    private State state;
    private int destFloor;


    public ElevatorImpl() {
    }

    public ElevatorImpl(int elevID, EventBus eventBus) {
        this.id = elevID;
        this.direction = Direction.NONE;
        this.state = State.IDLE;

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

        return destFloor;
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
        } else {
            this.direction = Direction.DOWN;
            this.state = State.MOVING_DOWN;
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
    public int currentFloor() {
        return currFloor;
    }

    @Override
    public void run() {
        move();
    }

    public void move() {
        System.out.println("READY");
        if (direction.equals(Direction.UP)) {
            currFloor++;
            System.out.println("Going Up" + currFloor);
        } else if (direction.equals(Direction.DOWN)) {
            currFloor--;

        }
    }
    @Subscribe
    public void getEvent(Elevator elevator){
        System.out.println("Got Event");
    }
}
