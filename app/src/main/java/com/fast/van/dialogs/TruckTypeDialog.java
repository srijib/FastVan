package com.fast.van.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.common.DebugLog;
import com.fast.van.models.signup.Truck;
import com.fast.van.models.signup.TruckType;
import com.fast.van.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class TruckTypeDialog {
    /**
     * @param act
     * @return
     */
    private static ArrayList<Truck> trucks = new ArrayList<>();


    static Truck flatDeckType, dropSideType, curtainSideType, pantechType;

    public static ConDialogActivity WithActivity(Activity act) {


        return new ConDialogActivity(act, (TruckDialogListener) act);
    }


    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private Activity activity;
        private TruckDialogListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogActivity(Activity activity, TruckDialogListener confirmationDialogEvents) {
            this.activity = activity;
            this.confirmationDialogEvents = confirmationDialogEvents;
        }


        /**
         * @param confirmationDialogCode
         */
        private boolean allchange;

        public void Show(final ConfirmationDialogCodes confirmationDialogCode) {
            try {

                final ArrayList<CheckBox> checkArray = new ArrayList<CheckBox>();
                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.truck_type_dailog);
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
                btnOk.setEnabled(false);
                CheckBox flatDeck = (CheckBox) dialog.findViewById(R.id.checkboxflatDeck);
                CheckBox dropSide = (CheckBox) dialog.findViewById(R.id.checkboxdropSide);
                CheckBox curtainSide = (CheckBox) dialog.findViewById(R.id.checkboxcurtainSide);
                final CheckBox pantech = (CheckBox) dialog.findViewById(R.id.checkboxpantech);
                final HashMap<String, Truck> truckHashMap = new HashMap<>();
                final String flatDeckKey = "flatDeck", dropSideKey = "dropSide", curtainSideKey = "curtainSide", pantechKey = "pantech";


                checkArray.add(flatDeck);
                checkArray.add(dropSide);
                checkArray.add(curtainSide);
                checkArray.add(pantech);


                flatDeck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            removehash_unselectToo("Flat Deck", checkArray);

                            if (flatDeckType == null) {
                                flatDeckType = new Truck(TruckType.FLAT_DECK);
                                truckHashMap.put(flatDeckKey, flatDeckType);
                            } else
                                truckHashMap.put(flatDeckKey, flatDeckType);
                        } else {
                            truckHashMap.remove(flatDeckKey);
                        }
                        saveEnble(truckHashMap.size(), btnOk);

                    }
                });
                dropSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {

                            removehash_unselectToo("Drop Side", checkArray);
                            if (dropSideType == null) {
                                dropSideType = new Truck(TruckType.DROP_SIDE);
                                truckHashMap.put(dropSideKey, dropSideType);
                            } else
                                truckHashMap.put(dropSideKey, dropSideType);
                        } else {
                            truckHashMap.remove(dropSideKey);
                        }
                        saveEnble(truckHashMap.size(), btnOk);

                    }
                });
                curtainSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {

                            removehash_unselectToo("Curtain Side", checkArray);
                            if (curtainSideType == null) {
                                curtainSideType = new Truck(TruckType.CURTAIN_SIDE);
                                truckHashMap.put(curtainSideKey, curtainSideType);
                            } else
                                truckHashMap.put(curtainSideKey, curtainSideType);
                        } else {
                            truckHashMap.remove(curtainSideKey);
                        }
                        saveEnble(truckHashMap.size(), btnOk);

                    }
                });

                pantech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {

                            removehash_unselectToo("Pantech", checkArray);
                            if (pantechType == null) {
                                pantechType = new Truck(TruckType.PANTECH);
                                truckHashMap.put(pantechKey, pantechType);
                            } else
                                truckHashMap.put(pantechKey, pantechType);
                        } else {
                            truckHashMap.remove(pantechKey);
                        }
                        saveEnble(truckHashMap.size(), btnOk);

                    }
                });

                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        dialog.dismiss();
                        StringBuilder list = new StringBuilder();
                        trucks.clear();
                        if (truckHashMap.get(flatDeckKey) != null) {
                            trucks.add(truckHashMap.get(flatDeckKey));
                            list.append(context.getString(R.string.flatDeck) + ", ");
                        }
                        if (truckHashMap.get(dropSideKey) != null) {
                            trucks.add(truckHashMap.get(dropSideKey));
                            list.append(context.getString(R.string.dropSide) + ", ");
                        }
                        if (truckHashMap.get(curtainSideKey) != null) {
                            trucks.add(truckHashMap.get(curtainSideKey));
                            list.append(context.getString(R.string.curtainSide) + ", ");
                        }
                        if (truckHashMap.get(pantechKey) != null) {
                            trucks.add(truckHashMap.get(pantechKey));
                            list.append(context.getString(R.string.pantech) + ", ");
                        }

                        String string = list.toString();
                        string = string.substring(0, string.length() - 2);
                        confirmationDialogEvents.onTruckTypeSelection(trucks, string);
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

                DebugLog.console("[TruckTypeDialog]:inside removehash_unselectToo with value:" + checkArray.size());

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
                DebugLog.console(ex, "[TruckTypeDialog]:Exception inside removehash_unselectToo");
            }
        }

        private void enableDisableView(int count, View view, boolean enabled) {
            Log.d("test", allchange + "count:" + count);
            if (view instanceof CheckBox)
                ((CheckBox) view).setChecked(enabled);

            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;

                for (int idx = 0; idx < group.getChildCount(); idx++) {
                    enableDisableView(count, group.getChildAt(idx), enabled);
                }
            }
        }

        private void saveEnble(int count, View view) {

            if (count > 0) {
                view.setEnabled(true);
            } else
                view.setEnabled(false);

        }

        private void allstatus(int count, CheckBox view) {
            Log.d("test", allchange + "allstatus count:" + count);
            if (count < 3) {
                allchange = false;
                view.setChecked(false);
            } else if (count == 3 && view.isChecked()) {

                view.setChecked(false);
            } else {
                allchange = false;
                view.setChecked(true);
            }

        }
    }

    public interface TruckDialogListener extends ConfirmationDialogEventsListener {
        void onTruckTypeSelection(ArrayList<Truck> trucks, String trucksString);
    }
}
