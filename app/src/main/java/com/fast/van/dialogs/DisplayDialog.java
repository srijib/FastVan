package com.fast.van.dialogs;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Amandeep Singh Bagli on 22/09/15.
 */
public class DisplayDialog {


   public static void show(Activity activity,int errorcode){
switch (errorcode){
    case 200:
        //ok
        break;
    case 201:
        CommonDialog.With(activity).show("user created");
        //created
        break;
    case 400:

        CommonDialog.With(activity).show("Bad request");
        //bad request

        break;
    case 401:
        CommonDialog.With(activity).show("you are unauthorized user");
       //unauthorized
        break;
    case 403:
        CommonDialog.With(activity).show("fornidden");
    //fornidden
        break;
    case 404:
        CommonDialog.With(activity).show("User not found");
    //customer not found
        break;
    case 409:
        CommonDialog.With(activity).show("User Already Exists");
        //already exists conflict
        break;
    case 500:
        CommonDialog.With(activity).show("internal server error");
        //internal server error
        break;

}

   }


}
