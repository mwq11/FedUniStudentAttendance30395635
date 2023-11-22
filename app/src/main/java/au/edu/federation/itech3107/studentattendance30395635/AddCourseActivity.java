package au.edu.federation.itech3107.studentattendance30395635;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.databinding.ActivityAddCourseBinding;

public class AddCourseActivity extends AppCompatActivity {

    private ActivityAddCourseBinding inflate;
    private ClassCheckAdapter mAdapter;
    private CourseBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        initView();
    }

    private void initView() {
        inflate.rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ClassCheckAdapter(R.layout.item_check_course, new ArrayList<>());
        inflate.rv.setAdapter(mAdapter);
        List<ClassBean> allClass = new DBOpenHelper().getAllClass(this);
        if (allClass != null) {
            mAdapter.setNewData(allClass);
        }

        bean = (CourseBean) getIntent().getSerializableExtra("bean");
        if (bean == null) {
            inflate.btnDelete.setVisibility(View.GONE);
        } else {
            inflate.etName.setText(bean.getCourseName());
            inflate.date.setText(bean.getDate());
            inflate.etTeacher.setText(bean.getTeacher());
        }

        inflate.date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    String desc = String.format("%d-%d-%d",year,month+1,day);
                    inflate.date.setText(desc);
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MARCH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        inflate.btnDelete.setOnClickListener(v -> {
            new DBOpenHelper().deleteCourse(this, bean);
        });

        inflate.btnSubmit.setOnClickListener(v -> {
            if (bean == null) {
                bean = new CourseBean();
                bean.setCourseName(inflate.etName.getText().toString());
                bean.setDate(inflate.date.getText().toString());
                bean.setTeacher(inflate.etTeacher.getText().toString());
                bean.setJoinClassId(CourserFragment.select_class);
                new DBOpenHelper().insertCourse(this, bean);
            }else {
                bean.setCourseName(inflate.etName.getText().toString());
                bean.setDate(inflate.date.getText().toString());
                bean.setTeacher(inflate.etTeacher.getText().toString());
                bean.setJoinClassId(CourserFragment.select_class);
                new DBOpenHelper().updateCourse(this, bean);
            }

            Toast.makeText(AddCourseActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public class ClassCheckAdapter extends BaseQuickAdapter<ClassBean, BaseViewHolder> {

        public ClassCheckAdapter(int layoutResId, @Nullable List<ClassBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ClassBean item) {
            helper.setText(R.id.tv_item, item.getName());
            CheckBox view = helper.getView(R.id.cb);
            view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (!CourserFragment.select_class.contains(item.getId() + ",")) {
                            CourserFragment.select_class = CourserFragment.select_class + item.getId() + ",";
                            Log.e("hao", "选中: " + CourserFragment.select_class);
                        }
                    } else {
                        if (CourserFragment.select_class.contains(item.getId() + ",")) {
                            String[] split = CourserFragment.select_class.split(",");
                            String newDes = "";
                            for (String i : split) {
                                if (!StringUtil.isEmpty(i)) {
                                    int i1 = Integer.parseInt(i);
                                    if (item.getId() != i1) {
                                        newDes = newDes + i1 + ",";
                                    }
                                }
                            }
                            CourserFragment.select_class = newDes;
                            Log.e("hao", "不选中: " + CourserFragment.select_class);
                        }
                    }
                }
            });
            view.setChecked(false);
            String selectClass = CourserFragment.select_class;
            if (!StringUtil.isEmpty(selectClass)) {
                String[] split = selectClass.split(",");
                for (String s : split) {
                    if (!StringUtil.isEmpty(s)) {
                        if ((s + ",").equals(item.getId() + ",")) {
                            view.setChecked(true);
                        }
                    }
                }
            }
        }
    }
}