package com.tingco.codechallenge.elevator.api;

/**
 * Interface for an elevator object.
 *
 * @author Sven Wesley
 *
 */
public interface Elevator {

    /**
     * Enumeration for describing elevator's direction.
     */
    enum Direction {
        UP, DOWN, NONE
    }
    /**
     * Enumeration for describing elevator's state.
     */
    enum State {
        MOVING_UP, MOVING_DOWN,IDLE,OCCUPIED,DOOR_CLOSED,DOOR_OPEN
    }
    /**
     * Tells the State of the Elevator.
     *
     * @return State Enumeration value describing the state.
     */
    State getState();
    /**
     * Tells which direction is the elevator going in.
     *
     * @return Direction Enumeration value describing the direction.
     */
    Direction getDirection();

    /**
     * If the elevator is moving. This is the target floor.
     *
     * @return primitive integer number of floor
     */
    int getAddressedFloor();

    /**
     * Get the Id of this elevator.
     *
     * @return primitive integer representing the elevator.
     */
    int getId();

    /**
     * Command to move the elevator to the given floor.
     *
     * @param toFloor
     *            int where to go.
     */
    void moveElevator(int toFloor);

    /**
     * Check if the elevator is occupied at the moment.
     *
     * @return true if busy.
     */
    boolean isBusy();

    /**
     * Reports which floor the elevator is at right now.
     *
     * @return int actual floor at the moment.
     */
    int getCurrentFloor();


}
