package com.ironhack.banking_system.data.model.accounts;

public enum AccountStatus {
    FROZEN, ACTIVE;

    public static AccountStatus fromString(String status) {
        return switch (status) {
            case "ACTIVE" -> ACTIVE;
            case "FROZEN" -> FROZEN;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }

}
