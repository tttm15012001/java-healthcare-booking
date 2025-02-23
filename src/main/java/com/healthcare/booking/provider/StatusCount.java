package com.healthcare.booking.provider;

import lombok.Getter;

@Getter
public class StatusCount {

    private int status;
    private long count;

    public StatusCount(int status, long count) {
        this.status = status;
        this.count = count;
    }

}
