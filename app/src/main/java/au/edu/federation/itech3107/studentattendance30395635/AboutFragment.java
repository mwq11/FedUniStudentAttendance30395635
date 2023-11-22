package au.edu.federation.itech3107.studentattendance30395635;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.databinding.FragmentAboutBinding;


public class AboutFragment extends AppCompatDialogFragment {

    private FragmentAboutBinding bind;
    private UserAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = FragmentAboutBinding.inflate(inflater, container, false);
        initView();
        return bind.getRoot();
    }

    private void initView() {
        bind.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getContext(), new MineFragment.onClickConfirmListener() {
                    @Override
                    public void onClick(String text) {
                        MainActivity activity = (MainActivity) getActivity();
                        UserBean userBean = activity.userBean;
                        userBean.setPwd(text);
                        new DBOpenHelper().updateUser(getContext(), userBean);
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bind.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new UserAdapter(R.layout.item_course, new ArrayList<>());
        bind.rv.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<CourseBean> user = new DBOpenHelper().getCourse(getContext());
        if (user != null) {
            mAdapter.setNewData(user);
        }
    }

    public class UserAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

        public UserAdapter(int layoutResId, @Nullable List<CourseBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CourseBean item) {
            helper.addOnClickListener(R.id.delete);
            helper.getView(R.id.check).setVisibility(View.GONE);
            helper.getView(R.id.delete).setVisibility(View.GONE);
            String rate = "";
            String joinClassId = item.getJoinClassId();
            String checkInStudentIds = item.getCheckInStudentIds();
            int allNum = 0;
            int checkNum = 0;
            if (StringUtil.isEmpty(joinClassId) || StringUtil.isEmpty(checkInStudentIds)) {
                rate = "0%";
            }else {
                String[] split = joinClassId.split(",");
                for(int i = 0; i < split.length; i++) {
                    if (!StringUtil.isEmpty(split[i])) {
                        List<StudentBean> list = new DBOpenHelper().getAllStudent(mContext, Integer.parseInt(split[i]));
                        if (list != null) {
                            Log.e("hao", "item.getId(): "+item.getId());
                            Log.e("hao", "size: "+list.size());
                            allNum += list.size();
                        }
                    }
                }

                String[] split1 = checkInStudentIds.split(",");
                for(int i = 0; i < split1.length; i++) {
                    if (!StringUtil.isEmpty(split1[i])){
                        checkNum++;
                    }
                }
                Log.e("hao", "checkNum: "+checkNum);
                Log.e("hao", "allNum: "+allNum);
                rate = (checkNum * 100 / allNum) + "%";
            }

            helper.setText(R.id.tv_item, "Course: " + item.getCourseName() + "   Rate: " +rate);
        }
    }

    public Dialog showDialog(Context context, MineFragment.onClickConfirmListener listener) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_upload, null);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        EditText et = (EditText) view.findViewById(R.id.et);
        et.setHint(context.getString(R.string.password));
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
                if (StringUtil.isEmpty(trim)) {
                    Toast.makeText(context, context.getString(R.string.enter_class), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onClick(trim);
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }


    public interface onClickConfirmListener {
        void onClick(String text);
    }
}
