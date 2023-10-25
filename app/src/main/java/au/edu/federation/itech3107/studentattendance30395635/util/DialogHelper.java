package au.edu.federation.itech3107.studentattendance30395635.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.lang.reflect.Field;

import au.edu.federation.itech3107.studentattendance30395635.R;



public class DialogHelper {

    private ProgressDialog progressDialog;
    //Easy access to the DialogHelper object outside the callback function to close the mCustomDialog
    private AlertDialog mCustomDialog;

    /**
     * You cannot close the waiting dialog if you are not sure<br>
     */
    public void showProgressDialog(Context context, String title, String msg, boolean canceable) {
        hideCustomDialog();

        progressDialog = ProgressDialog.show(context, title, msg, true, canceable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * General dialog box
     */
    public void showNormalDialog(@NonNull Activity activity, @NonNull String title,
                                 @NonNull String massage, @NonNull final DialogListener listener) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(massage)
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onPositive(dialog, which);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegative(dialog, which);
                            }
                        })
                .show();
    }

    /**
     * List dialog box
     */
    public void showListDialog(@NonNull Activity activity, String title,
                               @NonNull String[] items, @NonNull final DialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onItemClick(dialog, which);
            }
        }).show();
    }


    /**
     * Customize the pop-up
     */
    public void showCustomDialog(@NonNull Context context, View dialogView, String title,
                                 final DialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(dialogView);


        if (title != null) {
            builder.setTitle(title);
        }

        if (listener != null) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onPositive(dialog, which);
                }
            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onNegative(dialog, which);
                            dialog.dismiss();
                        }
                    });
        }

        //Set the location form size here
        mCustomDialog = builder.create();

        try {
            Field field = field = mCustomDialog.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);

            //   Gets the value of mAlert variable
            Object obj = field.get(mCustomDialog);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);

            //   Modify the value of the mHandler variable, using the new ButtonHandler class
            field.set(obj, new ButtonHandler(mCustomDialog));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCustomDialog.show();
    }

    public void showCustomDialog(@NonNull Context context, View dialogView, String title, int dialogWidth,
                                 final DialogListener listener) {
        showCustomDialog(context, dialogView, title, listener);
        Window window = mCustomDialog.getWindow();
        if (window != null) {
            window.setLayout(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * Bottom popup
     */
    public Dialog buildBottomDialog(Activity activity, View layoutView) {
        Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);
        bottomDialog.setContentView(layoutView);
        ViewGroup.LayoutParams layoutParams = layoutView.getLayoutParams();
        layoutParams.width = activity.getResources().getDisplayMetrics().widthPixels;
        layoutView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        return bottomDialog;
    }

    /**
     * The bottom list popup
     */
    public Dialog buildBottomListDialog(Activity activity, String[] items, final DialogListener listener) {
        ListView listView = new ListView(activity.getApplicationContext());
        listView.setDivider(new ColorDrawable(activity.getResources().getColor(R.color.color_divider)));
        listView.setDividerHeight(1);
        listView.setBackgroundColor(activity.getResources().getColor(R.color.white_f1));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.adapter_bottom_dialog_sytle, items);
        listView.setAdapter(adapter);


        final Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(bottomDialog, position);
            }
        });

        bottomDialog.setContentView(listView);
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.width = activity.getResources().getDisplayMetrics().widthPixels;
        listView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        return bottomDialog;
    }


    public void hideCustomDialog() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
            mCustomDialog = null;
        }
    }
}