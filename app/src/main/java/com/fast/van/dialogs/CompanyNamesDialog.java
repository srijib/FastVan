package com.fast.van.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.fast.van.R;
import com.fast.van.adapters.CompanyAdapter;
import com.fast.van.models.signup.Company;
import com.fast.van.models.signup.CompanyList;

import java.util.ArrayList;

/**
 * Created by Amandeep Singh Bagli on 29/09/15.
 */
public class CompanyNamesDialog {
    /**
     * @param act
     * @return
     */


    public static ConDialogActivity WithActivity(Activity act) {


        return new ConDialogActivity(act, (CompanyDialogListener) act);
    }


    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private Activity activity;
        private CompanyDialogListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogActivity(Activity activity, CompanyDialogListener confirmationDialogEvents) {
            this.activity = activity;
            this.confirmationDialogEvents = confirmationDialogEvents;
        }


        /**
         * @param companyList
         * @param confirmationDialogCode
         */

        public void show(CompanyList companyList, final ConfirmationDialogCodes confirmationDialogCode) {
            try {


                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.company_name_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);


//                TextView textMessage = (TextView) dialog
//                        .findViewById(R.id.textMessage);
                //textMessage.setText(message);


                final CompanyAdapter adapter = setupRecyclerView(companyList, dialog);
                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);


                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.onCompanySelection(adapter.getSelection());
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

    }

    private static CompanyAdapter setupRecyclerView(CompanyList companyList, final Dialog dialog) {
        RecyclerView mDrawerList = (RecyclerView) dialog.findViewById(R.id.companyRecyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setHasFixedSize(true);


        ArrayList<String> list = new ArrayList<>();
        if (companyList != null && companyList.getData() != null && companyList.getData().length > 0) {
            for (Company company : companyList.getData()) {
                list.add(company.getCompanyName());
            }
        }
        CompanyAdapter adapter = new CompanyAdapter(dialog.getContext(), list);

        mDrawerList.setAdapter(adapter);
        return adapter;
    }

    public interface CompanyDialogListener extends ConfirmationDialogEventsListener {
        void onCompanySelection(String companyName);
    }
}
