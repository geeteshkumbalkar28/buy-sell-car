package com.spring.jwt.entity;

public enum Status {
    PENDING("Pending"),
    ACTIVE("Active"),
    SOLD("Sold"),
    DEACTIVATE("Deactivate");


    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }
}

