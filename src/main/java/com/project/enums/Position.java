package com.project.enums;

import java.util.Map;

public enum Position{
    GK("Goalkeeper"),
    DF("Defender"),
    MF("Midfielder"),
    FW("Forward");

    private final String value;

    Position(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    private static final Map<String, Position> MAP = Map.of(
            "GK", GK,
            "DF", DF,
            "MF", MF,
            "FW", FW
    );

    public static Position fromString(String input) {
        if (input == null || input.trim().isEmpty() || input.equalsIgnoreCase("All")) {
            return null;
        }

        Position position = MAP.get(input);
        if (position == null) {
            throw new IllegalArgumentException("Invalid position: " + input);
        }
        return position;
    }
}