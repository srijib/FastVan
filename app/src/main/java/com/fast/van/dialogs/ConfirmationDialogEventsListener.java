package com.fast.van.dialogs;

public interface ConfirmationDialogEventsListener {


    void OnOkButtonPressed(ConfirmationDialogCodes confirmationDialogCode);

    void OnCancelButtonPressed(ConfirmationDialogCodes confirmationDialogCode);

}