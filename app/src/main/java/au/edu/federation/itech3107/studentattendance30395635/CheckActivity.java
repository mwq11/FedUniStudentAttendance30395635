package au.edu.federation.itech3107.studentattendance30395635;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.databinding.ActivityCheckBinding;


public class CheckActivity extends AppCompatActivity {
    private ActivityCheckBinding inflate;
    private CheckAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivityCheckBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        initView();
    }

    private void initView() {
        inflate.rv.setLayoutManager(new LinearLayoutManager(this));
        inflate.rv.setNestedScrollingEnabled(false);

        mAdapter = new CheckAdapter(R.layout.item_check, new ArrayList<>(), CheckActivity.this);
        inflate.rv.setAdapter(mAdapter);

        CourseBean bean = (CourseBean) getIntent().getSerializableExtra("bean");

        inflate.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setJoinClassId(CourserFragment.select_class);
                bean.setCheckInStudentIds(CourserFragment.select_student);
                Log.e("hao", "CheckActivity onClick(): " + CourserFragment.select_student);
                new DBOpenHelper().updateCourse(CheckActivity.this, bean);
                Toast.makeText(CheckActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        List<ClassBean> list = new DBOpenHelper().getAllClass(this);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (CourserFragment.select_class.contains(list.get(i).getId() + ",")) {
                    mAdapter.addData(list.get(i));
                }
            }
        }

    }

    public class CheckAdapter extends BaseQuickAdapter<ClassBean, BaseViewHolder> {

        private Context mContext;

        public CheckAdapter(int layoutResId, @Nullable List<ClassBean> data, Context context) {
            super(layoutResId, data);
            mContext = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, ClassBean item) {
            helper.setText(R.id.tv_item, "Class Name: " + item.getName());
            RecyclerView view = helper.getView(R.id.rv);
            view.setLayoutManager(new LinearLayoutManager(mContext));
            view.setNestedScrollingEnabled(false);
            StudentCheckAdapter mAdapter = new StudentCheckAdapter(R.layout.item_check_course, new ArrayList<>());
            view.setAdapter(mAdapter);
            List<StudentBean> list = new DBOpenHelper().getAllStudent(mContext, item.getId());
            if (list != null) {
                mAdapter.setNewData(list);
            }

        }
    }

    public class StudentCheckAdapter extends BaseQuickAdapter<StudentBean, BaseViewHolder> {

        public StudentCheckAdapter(int layoutResId, @Nullable List<StudentBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, StudentBean item) {
            helper.setText(R.id.tv_item,item.getName());
            CheckBox view = helper.getView(R.id.cb);
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int id = item.getId();
                if (StringUtil.isEmpty(CourserFragment.select_student)) {
                    CourserFragment.select_student = "";
                }
                if (b) {
                    if (!CourserFragment.select_student.contains(id+",")) {
                        CourserFragment.select_student = CourserFragment.select_student + id + ",";
                        Log.e("hao", "选中: "+CourserFragment.select_student);
                    }
                }else {
                    if (CourserFragment.select_student.contains(id+",")) {
                        String[] split = CourserFragment.select_student.split(",");
                        String newDes = "";
                        for (String i : split){
                            if (!StringUtil.isEmpty(i)){
                                int i1 = Integer.parseInt(i);
                                if (id != i1){
                                    newDes = newDes + i1+",";
                                }
                            }
                        }
                        CourserFragment.select_student = newDes;
                        Log.e("hao", "不选中: "+CourserFragment.select_student);
                    }
                }
            }
        });
        view.setChecked(false);
        String selectClass = CourserFragment.select_student;
        if (!StringUtil.isEmpty(selectClass)) {
            String[] split = selectClass.split(",");
            for (String s : split) {
                if (!StringUtil.isEmpty(s)) {
                    if ((s+",").equals(item.getId()+",")) {
                        view.setChecked(true);
                    }
                }
            }
        }
        }
    }
}
