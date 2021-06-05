package com.tingco.codechallenge.elevator.api.beans;

public class ElevatorEvent {
    private int elevatorId;
    private int destFloor;
    private EventType eventType;

    public ElevatorEvent(int elevatorId, EventType eventType) {
        this.elevatorId = elevatorId;
        this.eventType = eventType;
    }
    public ElevatorEvent(int elevatorId,int destFloor, EventType eventType) {
        this.elevatorId = elevatorId;
        this.eventType = eventType;
        this.destFloor=destFloor;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getDestFloor() {
        return destFloor;
    }
}
