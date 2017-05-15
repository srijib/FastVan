package com.fast.van.models.enums;

public enum BookingStatus {









    ONGOING(1), CONFIRMED(2), COMPLETED(3),
    CANCELLED(4), ONTHEWAY(5), DELIVERED(6), CHECKEDOUT(7);

    private int status;

    BookingStatus(int numVal) {
        this.status = numVal;
    }

    public int getStatus() {
        return status;
    }
}

//public enum BookingStatus {
//
//
//    MODIFIED_SUCCESSFULLY(0), ONGOING(1), CONFIRMED(2), COMPLETED(3),
//    CANCELLED(4), ONTHEWAY(5), DELIVERED(6), CHECKEDOUT(7), REJECTED(8),
//    PAYMENT_PENDING(9), PAYMENT_UNSUCCESSFUL(10), PAYMENT_SUCCESSFUL_BOOKING_INACTIVE(11),
//    NEW(12), TO_BE_APPROVED(13), APPROVED(14), INVOICE_SENT(15);
//
//    private int status;
//
//    BookingStatus(int numVal) {
//        this.status = numVal;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//}