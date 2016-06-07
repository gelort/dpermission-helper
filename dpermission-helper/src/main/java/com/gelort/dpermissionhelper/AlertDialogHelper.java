package com.gelort.dpermissionhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by o.tretyakov on 06/05/16.
 */

/**
 * Alert dialog helper class
 */
public class AlertDialogHelper {

    public static AlertDialog getAlertDialog(Context context,
                                             int theme,
                                             String title,
                                             String message,
                                             String negativeButtonName,
                                             String positiveButtonName,
                                             DialogInterface.OnClickListener onNegativeClickListener,
                                             DialogInterface.OnClickListener onPositiveOnClickListener) {
        return new AlertDialog.Builder(context, theme)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeButtonName, onNegativeClickListener)
                .setPositiveButton(positiveButtonName, onPositiveOnClickListener)
                .create();
    }

    public static AlertDialog getAlertDialog(Context context,
                                             int theme,
                                             String message,
                                             String negativeButtonName,
                                             String positiveButtonName,
                                             DialogInterface.OnClickListener onPositiveOnClickListener) {
        return new AlertDialog.Builder(context, theme)
                .setMessage(message)
                .setNegativeButton(negativeButtonName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(positiveButtonName, onPositiveOnClickListener)
                .create();
    }

    public static AlertDialog.Builder getAlertDialogBuilder(Context context, int theme) {
        return new AlertDialog.Builder(context, theme);
    }
}
