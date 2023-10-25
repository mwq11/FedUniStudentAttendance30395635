package au.edu.federation.itech3107.studentattendance30395635.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import au.edu.federation.itech3107.studentattendance30395635.R;



public class DialogUtil {


    /**
     *
     * @param context
     * @return
     */
    public static Dialog showDialog(Context context, onClickConfirmListener listener) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_upload, null);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        EditText et = (EditText) view.findViewById(R.id.et);

        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        Window window = dialog.getWindow();
        window.setContentView(view);
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = et.getText().toString().trim();
                if (StringUtil.isEmpty(trim)){
                    Toast.makeText(context,context.getString(R.string.enter_class),Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onClick(trim);
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public interface onClickConfirmListener{
       void onClick(String text);
    }
}
