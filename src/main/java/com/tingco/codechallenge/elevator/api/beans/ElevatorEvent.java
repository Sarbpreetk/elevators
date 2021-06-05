package com.tingco.codechallenge.elevator.api.beans;

public class ElevatorEvent {
    private int id;
    private State state;
    /**
     * Enumeration for describing elevator's state.
     */
    enum State {
        MOVING_UP, MOVING_DOWN,IDLE,OCCUPIED
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
