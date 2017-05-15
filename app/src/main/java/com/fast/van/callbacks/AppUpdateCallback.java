package com.fast.van.callbacks;

import com.fast.van.models.login.Login;

/**
 * Created by Amandeep Singh Bagli on 22/03/16.
 */
public interface AppUpdateCallback {
    void onForceUpdate();
    void onCancelUpdate(Login userData,boolean isShowMessage);
}
