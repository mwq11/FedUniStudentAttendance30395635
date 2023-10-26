package au.edu.federation.itech3107.studentattendance30395635.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.AppUtils;
import au.edu.federation.itech3107.studentattendance30395635.Constant;
import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.adapter.ClassCheckAdapter;
import au.edu.federation.itech3107.studentattendance30395635.bean.CourseAncestor;
import au.edu.federation.itech3107.studentattendance30395635.room.ClassBean;
import au.edu.federation.itech3107.studentattendance30395635.room.CourseV2;
import au.edu.federation.itech3107.studentattendance30395635.room.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395635.util.Preferences;
import au.edu.federation.itech3107.studentattendance30395635.util.ScreenUtils;
import au.edu.federation.itech3107.studentattendance30395635.util.StringUtil;
import au.edu.federation.itech3107.studentattendance30395635.view.EditTextLayout;
import au.edu.federation.itech3107.studentattendance30395635.view.PopupWindowDialog;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean mAddMode = true;

    private CourseAncestor mAncestor;
    private CourseV2 mIntentCourseV2;

    private ImageView mIvAddLocation;
    private LinearLayout mLayoutLocationContainer;
    private ImageView mIvSubmit;
    private EditTextLayout mEtlName;
    private EditTextLayout mEtlTeacher;
    private LinearLayout ll_class;
    private RecyclerView rv;
    private Button btn_add;
    private ClassCheckAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        handleIntent();
        initView();

        initListener();
    }

    private void initView() {
        mEtlName = findViewById(R.id.etl_name);
        mEtlTeacher = findViewById(R.id.etl_teacher);

        mIvAddLocation = findViewById(R.id.iv_add_location);
        mLayoutLocationContainer = findViewById(R.id.layout_location_container);
        mIvSubmit = findViewById(R.id.iv_submit);
        ll_class = findViewById(R.id.ll_class);
        btn_add = findViewById(R.id.btn_add);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
        mAdapter = new ClassCheckAdapter(R.layout.item_check_course, new ArrayList<>());
        rv.setAdapter(mAdapter);
        mIvSubmit.setImageResource(R.drawable.ic_done_black_24dp);
        addLocation(false);

        btn_add.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).putExtra("type", 1));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                            mAdapter.setNewData(list);
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

    private void initListener() {
        mIvAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addLocation(true);
                startActivity(new Intent(AddCourseActivity.this, CheckActivity.class)
                        .putExtra("bean", mIntentCourseV2));
            }
        });
        mIvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        mAncestor = (CourseAncestor) intent.getSerializableExtra(Constant.INTENT_ADD_COURSE_ANCESTOR);
        if (mAncestor != null) {
            mAddMode = true;
        } else {
            mIntentCourseV2 = (CourseV2) intent.getSerializableExtra(Constant.INTENT_EDIT_COURSE);
            if (mIntentCourseV2 != null) {
                mAddMode = false; //is edit mode
                mIntentCourseV2.init();// Clicking from the desktop must have been initialized
                // from other locations not necessarily
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void submit() {
        //name
        String name = mEtlName.getText().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(AddCourseActivity.this, "Not Null", Toast.LENGTH_SHORT).show();
            return;
        }

        //teacher
        String teacher = mEtlTeacher.getText().trim();
        //group

        long couCgId = Preferences.getLong(getString(R.string.app_preference_current_cs_name_id), 0);
        int childCount = mLayoutLocationContainer.getChildCount();
        boolean hasLocation = false;
        for (int i = 0; i < childCount; i++) {
            View locationItem = mLayoutLocationContainer.getChildAt(i);
            Object obj = locationItem.getTag();

            if (obj != null) {
                hasLocation = true;
                CourseV2 courseV2 = (CourseV2) obj;
                courseV2.setCouName(name);
                courseV2.setCouTeacher(teacher);
                courseV2.setGroupId(Constant.selectId);

                if (mAddMode || courseV2.getCouId() == 0) {
                    courseV2.setCouCgId(couCgId);
                    courseV2.setJoinClassId(Constant.select_class);
                    courseV2.init();
                    //Insert course
                    UserDataBase.getInstance(this).getCourseDao().insert(courseV2);
                } else {
                    courseV2.setJoinClassId(Constant.select_class);
                    courseV2.init();
                    //Update course
                    UserDataBase.getInstance(this).getCourseDao().update(courseV2);
                }

            }
        }
        if (!hasLocation) {
            Toast.makeText(AddCourseActivity.this, "Time Is Null", Toast.LENGTH_SHORT).show();
        }

        if (mAddMode) {
            Toast.makeText(AddCourseActivity.this, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddCourseActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void addLocation(boolean closeable) {
        final LinearLayout locationItem = (LinearLayout) View.inflate(this,
                R.layout.layout_location_item, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = ScreenUtils.dp2px(8);

        if (closeable) {
            locationItem.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLayoutLocationContainer.removeView(locationItem);
                }
            });

            initEmptyLocation(locationItem);

        } else {// Create a default class time and place
            locationItem.findViewById(R.id.iv_clear).setVisibility(View.INVISIBLE);

            if (mAncestor != null) {
                // Screen click here

                CourseV2 defaultCourse = new CourseV2().setCouOnlyIdR(AppUtils.createUUID())
                        .setCouAllWeekR(Constant.DEFAULT_ALL_WEEK)
                        .setCouWeekR(mAncestor.getRow())
                        .setCouStartNodeR(mAncestor.getCol())
                        .setCouNodeCountR(mAncestor.getRowNum())
                        .init();

                initNodeInfo(locationItem, defaultCourse);
            } else if (mIntentCourseV2 != null) {
                // 编辑过来
                initNodeInfo(locationItem, mIntentCourseV2);

                mEtlName.setText(mIntentCourseV2.getCouName());
                mEtlTeacher.setText(mIntentCourseV2.getCouTeacher());
                Constant.select_class = "";
                String joinClassId = mIntentCourseV2.getJoinClassId();
                if (!StringUtil.isEmpty(joinClassId)) {
                    Constant.select_class = joinClassId;
                }
            } else {
                //
                initEmptyLocation(locationItem);
                mIvAddLocation.setVisibility(View.GONE);
            }
        }

        locationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLocationItem(locationItem);
            }
        });

        mLayoutLocationContainer.addView(locationItem, params);
    }

    private void initEmptyLocation(LinearLayout locationItem) {
        CourseV2 defaultCourse = new CourseV2().setCouOnlyIdR(AppUtils.createUUID())
                .setCouAllWeekR(Constant.DEFAULT_ALL_WEEK)
                .setCouAllWeekR(1 + "")
                .setCouStartNodeR(1)
                .setCouNodeCountR(1);
        initNodeInfo(locationItem, defaultCourse);
    }

    private void initNodeInfo(LinearLayout locationItem, CourseV2 courseV2) {
        TextView tvText = locationItem.findViewById(R.id.tv_text);
        String builder = Constant.WEEK_SINGLE[courseV2.getCouWeek() - 1] + "Week " +
                " Section" + courseV2.getCouStartNode() + "-" +
                (courseV2.getCouStartNode() + courseV2.getCouNodeCount() - 1);
        tvText.setText(builder);

        locationItem.setTag(courseV2);
    }

    private void clickLocationItem(final LinearLayout locationItem) {
        PopupWindowDialog dialog = new PopupWindowDialog();

        CourseV2 courseV2 = null;
        Object obj = locationItem.getTag();
        // has tag data
        if (obj != null && obj instanceof CourseV2) {
            courseV2 = (CourseV2) obj;
        } else {
            throw new RuntimeException("Course data tag not be found");
        }

        dialog.showSelectTimeDialog(this, courseV2, new PopupWindowDialog.SelectTimeCallback() {
            @Override
            public void onSelected(CourseV2 course) {
                StringBuilder builder = new StringBuilder();
                builder.append("weeks").append(Constant.WEEK_SINGLE[course.getCouWeek() - 1])
                        .append(" ").append(course.getCouStartNode()).append("-")
                        .append(course.getCouStartNode() + course.getCouNodeCount() - 1).append("section");
                if (!TextUtils.isEmpty(course.getCouLocation())) {
                    builder.append("【").append(course.getCouLocation()).append("】");
                }

                ((TextView) locationItem.findViewById(R.id.tv_text))
                        .setText(builder.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
