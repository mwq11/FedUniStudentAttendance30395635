package au.edu.federation.itech3107.studentattendance30395635;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.federation.itech3107.studentattendance30395635.databinding.ActivityAddStudentBinding;


public class AddStudentActivity extends AppCompatActivity {

    private ActivityAddStudentBinding bind;
    private int mId;
    private StudentBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityAddStudentBinding.inflate(getLayoutInflater());
        mId = getIntent().getIntExtra("id", 0);
        setContentView(bind.getRoot());
        initView();
    }

    private void initView() {
        boolean add = getIntent().getBooleanExtra("add", false);
        if (!add) {
            bean = (StudentBean) getIntent().getSerializableExtra("bean");
            if (bean != null) {
                bind.etName.setText(bean.getName());
                bind.etNumber.setText(bean.getNumber() + "");
                bind.btnDelete.setVisibility(View.VISIBLE);
            } else
                bind.btnDelete.setVisibility(View.GONE);
        } else
            bind.btnDelete.setVisibility(View.GONE);
        bind.btnSubmit.setOnClickListener(v -> {
            if (StringUtil.isEmpty(bind.etName.getText().toString()) || StringUtil.isEmpty(bind.etNumber.getText().toString())) {
                Toast.makeText(AddStudentActivity.this, "Not Null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (add) {
                //新增
                StudentBean bean1 = new StudentBean();
                bean1.setName(bind.etName.getText().toString());
                bean1.setClassId(mId);
                bean1.setNumber(Long.parseLong(bind.etNumber.getText().toString()));
                new DBOpenHelper().insertStudent(AddStudentActivity.this, bean1);
                Toast.makeText(AddStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //修改
                bean.setName(bind.etName.getText().toString());
                bean.setNumber(Long.parseLong(bind.etNumber.getText().toString()));
                new DBOpenHelper().updateStudent(AddStudentActivity.this, bean);
                Toast.makeText(AddStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        bind.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DBOpenHelper().deleteStudent(AddStudentActivity.this, bean);
                finish();
            }
        });
    }
}
