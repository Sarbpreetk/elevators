package com.tingco.codechallenge.elevator.api.beans;

public class ElevatorRequest {
    private int currFloor;
    private int destFloor;

    public int getCurrFloor() {
        return currFloor;
    }

    public void setCurrFloor(int currFloor) {
        this.currFloor = currFloor;
    }

    public int getDestFloor() {
        return destFloor;
    }

    public void setDestFloor(int destFloor) {
        this.destFloor = destFloor;
    }
}
