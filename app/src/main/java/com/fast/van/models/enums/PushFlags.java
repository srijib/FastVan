package com.fast.van.models.enums;


//DROPOFF_CHECKLIST_RECEIVED: 1,
//        DRIVER_ON_THE_WAY: 2,
//        ADMIN_CANCELLED_BOOKING: 3,
//        INVOICE_EMAILED: 4,
//        CUST_CANCEL_BOOKING: 5,
//        PICKUP_CHECKLIST_RECEIVED: 6

public enum PushFlags {

    DROPOFF_CHECKLIST_RECEIVED(1),
    DRIVER_ON_THE_WAY(2),
    ADMIN_CANCELLED_BOOKING(3),
    INVOICE_EMAILED(4),
    CUST_CANCEL_BOOKING(5),
    PICKUP_CHECKLIST_RECEIVED(6);

    private int ordinal;

    PushFlags(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }
}


