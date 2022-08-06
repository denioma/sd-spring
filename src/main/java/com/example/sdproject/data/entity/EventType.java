package com.example.sdproject.data.entity;

public enum EventType {
    START("Game started"),
    END("Game ended"),
    GOAL("Goal"),
    YELLOW("Yellow card"),
    RED("Red card"),
    INTERRUPT("Game interrupted"),
    RESUME("Game resumed");

    private final String displayValue;

    EventType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
