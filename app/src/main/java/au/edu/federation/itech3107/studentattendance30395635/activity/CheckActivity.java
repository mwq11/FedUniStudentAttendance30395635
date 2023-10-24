package au.edu.federation.itech3107.studentattendance30395635.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.Constant;
import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.adapter.CheckAdapter;
import au.edu.federation.itech3107.studentattendance30395635.databinding.ActivityCheckBinding;
import au.edu.federation.itech3107.studentattendance30395635.room.ClassBean;
import au.edu.federation.itech3107.studentattendance30395635.room.CourseV2;
import au.edu.federation.itech3107.studentattendance30395635.room.StudentBean;
import au.edu.federation.itech3107.studentattendance30395635.room.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395635.util.StringUtil;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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

        mAdapter = new CheckAdapter(R.layout.item_check, new ArrayList<>(),  CheckActivity.this);
        inflate.rv.setAdapter(mAdapter);

        CourseV2 bean = (CourseV2) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            UserDataBase.getInstance(this).getCourseDao().getCourseById(bean.getCouId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MaybeObserver<CourseV2>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(CourseV2 list) {
                            //查询到结果
                            if (!StringUtil.isEmpty(list.getJoinClassId())) {
                                Constant.select_class = list.getJoinClassId();
                            }
                            if (!StringUtil.isEmpty(list.getCheckInStudentIds())) {
                                Constant.select_student = list.getCheckInStudentIds();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

        }

        inflate.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setJoinClassId(Constant.select_class);
                bean.setCheckInStudentIds(Constant.select_student);
                Log.e("hao", "CheckActivity onClick(): "+Constant.select_student);
                UserDataBase.getInstance(CheckActivity.this).getCourseDao().update(bean);
                Toast.makeText(CheckActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        UserDataBase.getInstance(this).getClassDao().getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<ClassBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ClassBean> list) {
                        //查询到结果
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                if (Constant.select_class.contains(list.get(i).getId() + ",")) {
                                    mAdapter.addData(list.get(i));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
