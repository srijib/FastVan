package com.fast.van.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.common.DebugLog;
import com.fast.van.models.signup.VehicalType;
import com.fast.van.models.signup.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class VehicleDialog {
    /**
     * @param act
     * @return
     */

    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static Vehicle bikeVehicle, vanVehicle, oneTonVehicle, twoTonVehicle, fourTonVehicle, eightTonVehicle, twelveTonVehicle, thirtytwoTonVehicle;

    public static ConDialogActivity WithActivity(Activity act) {


        return new ConDialogActivity(act, (VehicleDialogListener) act);
    }


    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private Activity activity;
        private VehicleDialogListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogActivity(Activity activity, VehicleDialogListener confirmationDialogEvents) {
            this.activity = activity;
            this.confirmationDialogEvents = confirmationDialogEvents;
        }


        /**
         * @param confirmationDialogCode
         */

        public void Show(final ConfirmationDialogCodes confirmationDialogCode) {
            try {

                final ArrayList<CheckBox> checkArray = new ArrayList<CheckBox>();
                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.vehicle_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);


                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                //textMessage.setText(message);
                final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                final CheckBox bike = (CheckBox) dialog.findViewById(R.id.checkboxBike);
                final CheckBox van = (CheckBox) dialog.findViewById(R.id.checkboxVan);
                final CheckBox oneTon = (CheckBox) dialog.findViewById(R.id.checkboxOneTon);
                final CheckBox twoTon = (CheckBox) dialog.findViewById(R.id.checkboxTwoTon);
                final CheckBox fourTon = (CheckBox) dialog.findViewById(R.id.checkboxFourTon);
                final CheckBox eightTon = (CheckBox) dialog.findViewById(R.id.checkboxEightTon);
                final CheckBox twelveTon = (CheckBox) dialog.findViewById(R.id.checkboxTwelveTon);
                final CheckBox thirtytwoTon = (CheckBox) dialog.findViewById(R.id.checkboxThirtyTwoTon);
                final HashMap<String, Vehicle> vehicleHashMap = new HashMap<>();
                final String bikeKey = "bike", vanKey = "van", oneTonKey = "oneTon", twoTonKey = "twoTon", fourTonKey = "fourTon", eightTonKey = "eightTon", twelveTonKey = "twelveTon", thirtytwoTonKey = "thirtytwoTon";

                checkArray.add(bike);
                checkArray.add(van);
                checkArray.add(oneTon);
                checkArray.add(twoTon);
                checkArray.add(fourTon);
                checkArray.add(eightTon);
                checkArray.add(twelveTon);
                checkArray.add(thirtytwoTon);
                bike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            removehash_unselectToo("Bike", checkArray);
                            if (bikeVehicle == null) {
                                bikeVehicle = new Vehicle(VehicalType.BIKE);
                                vehicleHashMap.put(bikeKey, bikeVehicle);
                            } else
                                vehicleHashMap.put(bikeKey, bikeVehicle);
                        } else {
                            vehicleHashMap.remove(bikeKey);
                        }
                        saveEnble(vehicleHashMap.size(), btnOk);

                    }
                });
                van.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            removehash_unselectToo("Van", checkArray);
                            if (vanVehicle == null) {
                                vanVehicle = new Vehicle(VehicalType.VAN);
                                vehicleHashMap.put(vanKey, vanVehicle);
                            } else
                                vehicleHashMap.put(vanKey, vanVehicle);
                        } else {
                            vehicleHashMap.remove(vanKey);
                        }

                        saveEnble(vehicleHashMap.size(), btnOk);
                    }
                });
                oneTon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            removehash_unselectToo("1 Ton Truck", checkArray);
                            if (oneTonVehicle == null) {
                                oneTonVehicle = new Vehicle(VehicalType.ONE_TON_TRUCK);
                                vehicleHashMap.put(oneTonKey, oneTonVehicle);
                            } else
                                vehicleHashMap.put(oneTonKey, oneTonVehicle);
                        } else {
                            vehicleHashMap.remove(oneTonKey);
                        }
                        saveEnble(vehicleHashMap.size(), btnOk);
                    }
                });
                twoTon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            removehash_unselectToo("2 Ton Truck", checkArray);
                            if (twoTonVehicle == null) {
                                twoTonVehicle = new Vehicle(VehicalType.TWO_TON_TRUCK);
                                vehicleHashMap.put(twoTonKey, twoTonVehicle);
                            } else
                                vehicleHashMap.put(twoTonKey, twoTonVehicle);
                        } else {
                            vehicleHashMap.remove(twoTonKey);
                        }
                        saveEnble(vehicleHashMap.size(), btnOk);

                    }
                });
                fourTon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            removehash_unselectToo("4 Ton Truck", checkArray);
                            if (fourTonVehicle == null) {
                                fourTonVehicle = new Vehicle(VehicalType.FOUR_TON_TRUCK);
                                vehicleHashMap.put(fourTonKey, fourTonVehicle);
                            } else
                                vehicleHashMap.put(fourTonKey, fourTonVehicle);
                        } else {
                            vehicleHashMap.remove(fourTonKey);
                        }
                        saveEnble(vehicleHashMap.size(), btnOk);

                    }
                });

                //Eight Ton Truck

                eightTon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DebugLog.console("[VehicleDialog]:EightTon truck selected");
                        if (isChecked) {
                            removehash_unselectToo("8 Ton Truck", checkArray);

                            if (eightTonVehicle == null) {
                                eightTonVehicle = new Vehicle(VehicalType.EIGHT_TON_TRUCK);
                                vehicleHashMap.put(eightTonKey, eightTonVehicle);
                            } else
                                vehicleHashMap.put(eightTonKey, eightTonVehicle);
                        } else {
                            vehicleHashMap.remove(eightTonKey);
                        }
                        saveEnble(vehicleHashMap.size(), btnOk);

                    }
                });


                //end eight

                twelveTon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DebugLog.console("[VehicleDialog]:TwelveTon truck selected");
                        if (isChecked) {
                            removehash_unselectToo("12 Ton Truck", checkArray);

                            if (twelveTonVehicle == null) {
                                twelveTonVehicle = new Vehicle(VehicalType.TWELVE_TON_TRUCK);
                                vehicleHashMap.put(twelveTonKey, twelveTonVehicle);
                            } else
                                vehicleHashMap.put(twelveTonKey, twelveTonVehicle);
                        } else {
                            vehicleHashMap.remove(twelveTonKey);
                        }
                        saveEnble(vehicleHashMap.size(), btnOk);

                    }
                });
                thirtytwoTon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DebugLog.console("[VehicleDialog]:ThirtytwoTon truck selected");
                        if (isChecked) {
                            removehash_unselectToo("32 Ton Truck", checkArray);

                            if (thirtytwoTonVehicle == null) {
                                thirtytwoTonVehicle = new Vehicle(VehicalType.TWELVE_TON_TRUCK);
                                vehicleHashMap.put(thirtytwoTonKey, thirtytwoTonVehicle);
                            } else
                                vehicleHashMap.put(thirtytwoTonKey, thirtytwoTonVehicle);
                        } else {
                            vehicleHashMap.remove(thirtytwoTonKey);
                        }
                        saveEnble(vehicleHashMap.size(), btnOk);

                    }
                });

                btnOk.setEnabled(false);

                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();

                        dialog.dismiss();
                        vehicles.clear();
                        StringBuilder list = new StringBuilder();
                        if (vehicleHashMap.get(bikeKey) != null) {
                            vehicles.add(vehicleHashMap.get(bikeKey));
                            list.append(context.getString(R.string.bike) + ", ");
                        }
                        if (vehicleHashMap.get(vanKey) != null) {
                            vehicles.add(vehicleHashMap.get(vanKey));
                            list.append(context.getString(R.string.van) + ", ");
                        }
                        if (vehicleHashMap.get(oneTonKey) != null) {
                            vehicles.add(vehicleHashMap.get(oneTonKey));
                            list.append(context.getString(R.string.oneton) + ", ");
                        }
                        if (vehicleHashMap.get(twoTonKey) != null) {
                            vehicles.add(vehicleHashMap.get(twoTonKey));
                            list.append(context.getString(R.string.twoton) + ", ");
                        }
                        if (vehicleHashMap.get(fourTonKey) != null) {
                            vehicles.add(vehicleHashMap.get(fourTonKey));
                            list.append(context.getString(R.string.fourton) + ", ");
                        }
                        if (vehicleHashMap.get(eightTonKey) != null) {
                            vehicles.add(vehicleHashMap.get(eightTonKey));
                            list.append(context.getString(R.string.eightton) + ", ");
                        }
                        if (vehicleHashMap.get(twelveTonKey) != null) {
                            vehicles.add(vehicleHashMap.get(twelveTonKey));
                            list.append(context.getString(R.string.twelveton) + ", ");
                        }
                        if (vehicleHashMap.get(thirtytwoTonKey) != null) {
                            vehicles.add(vehicleHashMap.get(thirtytwoTonKey));
                            list.append(context.getString(R.string.thirtytwoton) + ", ");
                        }
                        try {
                            String string = list.toString();
                            string = string.substring(0, string.length() - 2);

                            confirmationDialogEvents.onVehicleSelection(vehicles, string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //      confirmationDialogEvents.OnOkButtonPressed(confirmationDialogCode);
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.OnCancelButtonPressed(confirmationDialogCode);
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void removehash_unselectToo(String selectedvehicle, ArrayList<CheckBox> checkArray) {
            try {

                DebugLog.console("[VehicleDialog]:inside removehash_unselectToo with value:" + checkArray.size());

                for (int i = 0; i < checkArray.size(); i++) {
//                        if (checkArray.get(i) != (CheckBox)view)  // assuming v is the View param passed in
//                    DebugLog.console("[VehicleDialog]:count");
                    if (checkArray.get(i).isChecked()) {
                        DebugLog.console(checkArray.get(i).getText().toString());
                        if (checkArray.get(i).getText().toString().equalsIgnoreCase(selectedvehicle)) {
                            //DO nothing and unselect others in else part
                        } else {
                            checkArray.get(i).setChecked(false);
                        }

                    }
                }


            } catch (Exception ex) {
                DebugLog.console(ex, "[VehicleDialog]:Exception inside removehash_unselectToo");
            }
        }

        private void saveEnble(int count, View view) {

            if (count > 0) {
                view.setEnabled(true);
            } else
                view.setEnabled(false);

        }
    }


    public interface VehicleDialogListener extends ConfirmationDialogEventsListener {
        void onVehicleSelection(ArrayList<Vehicle> service, String vehicleList);
    }
}
