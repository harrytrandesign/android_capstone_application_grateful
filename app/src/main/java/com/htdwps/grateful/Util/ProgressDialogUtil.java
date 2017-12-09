package com.htdwps.grateful.Util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by HTDWPS on 12/8/17.
 */

public class ProgressDialogUtil {

    // To show the class and to dismiss it when no longer needed via https://stackoverflow.com/questions/37428840/how-to-put-progress-dialog-in-seperate-class-and-call-in-every-activity-in-andro
    public static ProgressDialog showProgressDialog(Context context, String message) {

        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        progressDialog.show();

        return progressDialog;
    }

}
