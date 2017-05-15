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
import com.fast.van.models.signup.Service;
import com.fast.van.models.signup.ServiceType;
import com.fast.van.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class ServiceDialog {
    /**
     * @param act
     * @return
     */
    private static ArrayList<Service> services = new ArrayList<>();
    static Service courierService, removalService, deliveryService, allServicesService;

    public static ConDialogActivity WithActivity(Activity act) {


        return new ConDialogActivity(act, (ServiceDialogListener) act);
    }


    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private Activity activity;
        private ServiceDialogListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogActivity(Activity activity, ServiceDialogListener confirmationDialogEvents) {
            this.activity = activity;
            this.confirmationDialogEvents = confirmationDialogEvents;
        }


        /**
         * @param confirmationDialogCode
         */
        private boolean allchange;

        public void Show(final ConfirmationDialogCodes confirmationDialogCode) {
            try {


                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.service_dailog);
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
                CheckBox courier = (CheckBox) dialog.findViewById(R.id.checkboxCourier);
                CheckBox removal = (CheckBox) dialog.findViewById(R.id.checkboxRemoval);
                CheckBox delivery = (CheckBox) dialog.findViewById(R.id.checkboxDelivery);
                final CheckBox allservices = (CheckBox) dialog.findViewById(R.id.checkboxAllServices);
                final HashMap<String, Service> serviceHashMap = new HashMap<>();
                final String courierKey = "courier", removalKey = "removal", deliveryKey = "delivery", allServicesKey = "allServices";

                courier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (courierService == null) {
                                courierService = new Service(ServiceType.COURIER);
                                serviceHashMap.put(courierKey, courierService);
                            } else
                                serviceHashMap.put(courierKey, courierService);
                        } else {
                            serviceHashMap.remove(courierKey);
                        }
                        allchange = isChecked;
                        saveEnble(serviceHashMap.size(), btnOk);
                        allstatus(serviceHashMap.size(), allservices);
                    }
                });
                removal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (removalService == null) {
                                removalService = new Service(ServiceType.FREIGHT);
                                serviceHashMap.put(removalKey, removalService);
                            } else
                                serviceHashMap.put(removalKey, removalService);
                        } else {
                            serviceHashMap.remove(removalKey);
                        }
                        saveEnble(serviceHashMap.size(), btnOk);
                        allstatus(serviceHashMap.size(), allservices);
                    }
                });
                delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (deliveryService == null) {
                                deliveryService = new Service(ServiceType.DELIVERY);
                                serviceHashMap.put(deliveryKey, deliveryService);
                            } else
                                serviceHashMap.put(deliveryKey, deliveryService);
                        } else {
                            serviceHashMap.remove(deliveryKey);
                        }
                        saveEnble(serviceHashMap.size(), btnOk);
                        allstatus(serviceHashMap.size(), allservices);
                    }
                });

                allservices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*if(allservices.isChecked()){
                            if (allServicesService == null) {
                                allServicesService = new Service(ServiceType.ALL_SERVICES);
                                serviceHashMap.put(allServicesKey, allServicesService);
                            } else
                                serviceHashMap.put(allServicesKey, allServicesService);
                        }else {
                            serviceHashMap.remove(allServicesKey);

                        }*/
                        saveEnble(serviceHashMap.size(), btnOk);
                        enableDisableView(serviceHashMap.size(), dialog.findViewById(R.id.rl1), allservices.isChecked());
                    }
                });
                allservices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        /*if (isChecked) {
                            if (allServicesService == null) {
                                allServicesService = new Service(ServiceType.ALL_SERVICES);
                                serviceHashMap.put(allServicesKey, allServicesService);
                            } else
                                serviceHashMap.put(allServicesKey, allServicesService);
                        } else {
                            serviceHashMap.remove(allServicesKey);
                        }*/
                        saveEnble(serviceHashMap.size(), btnOk);
                        //   allstatus(serviceHashMap.size(), allservices);
                 /*   if(allchange) {
                        enableDisableView(serviceHashMap.size(), dialog.findViewById(R.id.rl1), isChecked);
                    }else
                        allchange=true;*/
                    }
                });


                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        dialog.dismiss();
                        StringBuilder list = new StringBuilder();
                        services.clear();
                        if (serviceHashMap.get(courierKey) != null) {
                            services.add(serviceHashMap.get(courierKey));
                            list.append(context.getString(R.string.courier) + ", ");
                        }
                        if (serviceHashMap.get(removalKey) != null) {
                            services.add(serviceHashMap.get(removalKey));
                            list.append(context.getString(R.string.removal) + ", ");
                        }
                        if (serviceHashMap.get(deliveryKey) != null) {
                            services.add(serviceHashMap.get(deliveryKey));
                            list.append(context.getString(R.string.delivery) + ", ");
                        }
                        if (serviceHashMap.get(allServicesKey) != null) {
                            services.add(serviceHashMap.get(allServicesKey));
                            list.append(context.getString(R.string.allservices) + ", ");
                        }

                        String string = list.toString();
                        string = string.substring(0, string.length() - 2);
                        confirmationDialogEvents.onServiceSelection(services, string);
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

    public interface ServiceDialogListener extends ConfirmationDialogEventsListener {
        void onServiceSelection(ArrayList<Service> service, String servicesString);
    }
}
