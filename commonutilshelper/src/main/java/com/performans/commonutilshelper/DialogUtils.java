package com.performans.commonutilshelper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogUtils {

    public static AlertDialog getDialogProgressBar(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.str_please_wait);
        final ProgressBar progressBar = new ProgressBar(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(30, 30, 30, 30);
        progressBar.setLayoutParams(lp);
        builder.setView(progressBar);
        return builder.create();
    }

    public static void showConfirm(Context context, String message, DialogInterface.OnClickListener okHandler) {
        showDialog2(context,
                context.getResources().getString(R.string.title_attention),
                message,
                okHandler,
                context.getResources().getString(android.R.string.cancel),
                null);
    }

    public static void showPermissionsDialog(Context context, DialogInterface.OnClickListener okHandler, DialogInterface.OnClickListener neutralHandler) {
        showDialog2(context,
                context.getResources().getString(R.string.title_attention),
                context.getResources().getString(R.string.str_permission_required),
                okHandler,
                context.getResources().getString(R.string.exitApp),
                neutralHandler);
    }

    public static void showPermissionsDialogToSettings(Context context, DialogInterface.OnClickListener okHandler, DialogInterface.OnClickListener neutralHandler) {
        showDialog2(context,
                context.getResources().getString(R.string.title_attention),
                context.getResources().getString(R.string.str_permission_required),
                okHandler,
                context.getResources().getString(R.string.action_go_to_settings),
                neutralHandler);
    }

    public static void showAlert(Context context, String title, String message, DialogInterface.OnClickListener okHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, okHandler);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDialog2(Context context, String title, String message,
                                   DialogInterface.OnClickListener okHandler,
                                   String neutralButtonText,
                                   DialogInterface.OnClickListener neutralHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, okHandler);
        builder.setNeutralButton(neutralButtonText, neutralHandler);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDialog(Context context, String title, String message,
                                  String okName,
                                  DialogInterface.OnClickListener okHandler,
                                  String cancelName,
                                  DialogInterface.OnClickListener cancelHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okName, okHandler);
        builder.setNegativeButton(cancelName, cancelHandler);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDialog(Context context, String title, String message,
                                  String okName,
                                  DialogInterface.OnClickListener okHandler,
                                  String neutralName,
                                  DialogInterface.OnClickListener neutralHandler,
                                  String cancelName,
                                  DialogInterface.OnClickListener cancelHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okName, okHandler);
        builder.setNeutralButton(neutralName, neutralHandler);
        builder.setNegativeButton(cancelName, cancelHandler);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static ProgressDialog showProgressDialog(Activity activity) {
        return showProgressDialog(activity, "Loading");
    }

    public static ProgressDialog showProgressDialog(Activity activity, String text) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(text);
        progressDialog.setProgressStyle(android.R.style.Theme_Holo_Dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        return progressDialog;
    }

    public static AlertDialog showAlertDialog(Context context, String message) {
        return showAlertDialog(context, null, message);
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message) {
        return showAlertDialog(context, title, message, -1);
    }

    public static AlertDialog showAlertDialog(Context context, String message, int icon) {
        return showAlertDialog(context, null, message, icon);
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, int icon) {
        return showAlertDialog(context, title, message, icon, null, false);
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener onOkClicked) {
        return showAlertDialog(context, title, message, -1, new DialogButton[]{new DialogButton("OK", DialogButton.ButtonTypes.POSITIVE, onOkClicked)}, false);
    }

    public static AlertDialog showAlertDialog(final Context context, String title, String message, int icon, DialogButton[] buttons, boolean buttonsAsItems) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        if (buttonsAsItems && buttons != null && buttons.length > 0) {
            ArrayList<String> items = new ArrayList<>();

            for (DialogButton button : buttons) {
                items.add(button.getText());
            }

            String[] aItems = items.toArray(new String[items.size()]);

            final ArrayList<DialogButton> dButtons = new ArrayList<DialogButton>(Arrays.asList(buttons));

            dialogBuilder.setItems(aItems, (dialog, which) -> dButtons.get(which).getOnClickListener().onClick(dialog, which));
        } else {
            if (buttons == null || buttons.length == 0) {
                buttons = new DialogButton[]{new DialogButton("OK")};
            }

            for (DialogButton button : buttons) {
                switch (button.getButtonType()) {
                    case NEUTRAL:
                        dialogBuilder.setNeutralButton(button.getText(), button.getOnClickListener());
                        break;
                    case NEGATIVE:
                        dialogBuilder.setNegativeButton(button.getText(), button.getOnClickListener());
                        break;
                    case POSITIVE:
                        dialogBuilder.setPositiveButton(button.getText(), button.getOnClickListener());
                        break;
                }
            }
        }


        if (title != null && !title.equals("")) dialogBuilder.setTitle(title);
        if (message != null && !message.equals("")) dialogBuilder.setMessage(message);
        if (icon != -1) dialogBuilder.setIcon(icon);

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setOnShowListener(dialog -> {
            int[] buttonTypes = new int[]{Dialog.BUTTON_POSITIVE, Dialog.BUTTON_NEGATIVE, Dialog.BUTTON_NEUTRAL};

            for (int btnType : buttonTypes) {
                Button btn = alertDialog.getButton(btnType);

                if (btn != null) {
                    btn.setTextColor(alertDialog.getContext().getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        });

        alertDialog.show();

        return alertDialog;
    }
}
