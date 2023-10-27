package au.edu.federation.itech3107.studentattendance30395635.activity;

import static au.edu.federation.itech3107.studentattendance30395635.util.ScreenUtils.dp2px;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.AppUtils;
import au.edu.federation.itech3107.studentattendance30395635.Constant;
import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.adapter.SelectWeekAdapter;
import au.edu.federation.itech3107.studentattendance30395635.bean.CourseAncestor;
import au.edu.federation.itech3107.studentattendance30395635.room.CourseV2;
import au.edu.federation.itech3107.studentattendance30395635.room.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395635.util.DialogHelper;
import au.edu.federation.itech3107.studentattendance30395635.util.DialogListener;
import au.edu.federation.itech3107.studentattendance30395635.util.Preferences;
import au.edu.federation.itech3107.studentattendance30395635.util.ScreenUtils;
import au.edu.federation.itech3107.studentattendance30395635.util.TimeUtils;
import au.edu.federation.itech3107.studentattendance30395635.util.Utils;
import au.edu.federation.itech3107.studentattendance30395635.view.CourseView;
import au.edu.federation.itech3107.studentattendance30395635.view.ShowDetailDialog;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvWeekCount;
    private int mCurrentWeekCount;
    private String mCurrentMonth;
    private ShowDetailDialog mDialog;
    private CourseView mCourseViewV2;
    private LinearLayout mLayoutWeekGroup;
    private LinearLayout mLayoutNodeGroup;
    private int WEEK_TEXT_SIZE = 12;
    private int NODE_TEXT_SIZE = 11;
    private int NODE_WIDTH = 28;
    private TextView mMMonthTextView;
    private RecyclerView mRvSelectWeek;
    private int mHeightSelectWeek;
    private boolean mSelectWeekIsShow = false;
    private LinearLayout mLayoutCourse;
    private int mIntentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mLayoutWeekGroup = findViewById(R.id.layout_week_group);
        mLayoutNodeGroup = findViewById(R.id.layout_node_group);
        mLayoutCourse = findViewById(R.id.layout_course);
        mIntentId = getIntent().getIntExtra("id", 0);
        mCurrentMonth = getIntent().getStringExtra("date");
        ScreenUtils.init(this);
        Preferences.init(this);
        initToolbar();
        initWeek();
        initCourseView();
        initWeekNodeGroup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void initWeek() {
        initWeekTitle();
        initSelectWeek();
    }

    private void initSelectWeek() {
        mRvSelectWeek = findViewById(R.id.recycler_view_select_week);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
        params.topMargin = -dp2px(45);
        mRvSelectWeek.setLayoutParams(params);

        mRvSelectWeek.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL, false));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            strings.add( i + "week");
        }
        SelectWeekAdapter selectWeekAdapter = new SelectWeekAdapter(R.layout.adapter_select_week, strings);
        mRvSelectWeek.setAdapter(selectWeekAdapter);
        mRvSelectWeek.scrollToPosition(AppUtils.getCurrentWeek(this)-1);

        mRvSelectWeek.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mHeightSelectWeek = bottom - top;
            }
        });

        selectWeekAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCurrentWeekCount = position + 1;

                AppUtils.PreferencesCurrentWeek(CourseActivity.this, mCurrentWeekCount);
                mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
                mCourseViewV2.resetView();
                mTvWeekCount.setText(mCurrentWeekCount + "week");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animSelectWeek(false);
                        AppUtils.updateWidget(getApplicationContext());
                    }
                }, 150);
            }
        });
    }


    private void animSelectWeek(boolean show) {
        mSelectWeekIsShow = show;

        int start = 0, end = 0;
        if (show) {
            start = -mHeightSelectWeek;
        } else {
            end = -mHeightSelectWeek;
        }

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
                params.topMargin = (int) animation.getAnimatedValue();
                mRvSelectWeek.setLayoutParams(params);
            }
        });
        animator.start();
    }


    private void initWeekTitle() {
        mTvWeekCount = findViewById(R.id.tv_toolbar_subtitle);
        mTvWeekCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animSelectWeek(!mSelectWeekIsShow);
            }
        });
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.class_schedule));
    }

    private void initWeekNodeGroup() {
        mLayoutNodeGroup.removeAllViews();
        mLayoutWeekGroup.removeAllViews();

        for (int i = -1; i < 7; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setGravity(Gravity.CENTER);

            textView.setWidth(0);
            textView.setTextColor(getResources().getColor(R.color.primary_text));
            LinearLayout.LayoutParams params;

            if (i == -1) {
                params = new LinearLayout.LayoutParams(
                        Utils.dip2px(getApplicationContext(), NODE_WIDTH),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setTextSize(NODE_TEXT_SIZE);
                textView.setText(mCurrentMonth + "\n月");

                mMMonthTextView = textView;
            } else {
                params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                textView.setTextSize(WEEK_TEXT_SIZE);
                textView.setText(Constant.WEEK_SINGLE[i]);
            }

            mLayoutWeekGroup.addView(textView, params);
        }

        int nodeItemHeight = Utils.dip2px(getApplicationContext(), 55);
        for (int i = 1; i <= 16; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setTextSize(NODE_TEXT_SIZE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            textView.setText(String.valueOf(i));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, nodeItemHeight);
            mLayoutNodeGroup.addView(textView, params);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCourseView() {
        mCourseViewV2 = findViewById(R.id.course_view_v2);
        mCourseViewV2.setCourseItemRadius(3)
                .setTextTBMargin(dp2px(1), dp2px(1));

        mCourseViewV2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("touch");
                return false;
            }
        });
        initCourseViewEvent();
    }

    /**
     * courseVIew
     */
    private void initCourseViewEvent() {
        mCourseViewV2.setOnItemClickListener(new CourseView.OnItemClickListener() {
            @Override
            public void onClick(List<CourseAncestor> course, View itemView) {
                mDialog = new ShowDetailDialog();
                mDialog.show(CourseActivity.this, (CourseV2) course.get(0), new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mDialog = null;
                    }
                });
            }

            @Override
            public void onLongClick(List<CourseAncestor> courses, View itemView) {
                final CourseV2 course = (CourseV2) courses.get(0);
                DialogHelper dialogHelper = new DialogHelper();
                dialogHelper.showNormalDialog(CourseActivity.this, getString(R.string.confirm_to_delete),
                        "Class 【" + course.getCouName() + "】" + Constant.WEEK[course.getCouWeek()]
                                + "" + course.getCouStartNode() + " ", new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog, int which) {
                                super.onPositive(dialog, which);
                                deleteCancelSnackBar(course);
                            }
                        });
            }

            public void onAdd(CourseAncestor course, View addView) {
                Intent intent = new Intent(CourseActivity.this, AddCourseActivity.class);
                intent.putExtra(Constant.INTENT_ADD_COURSE_ANCESTOR, course);
                intent.putExtra(Constant.INTENT_ADD, true);
                startActivity(intent);
            }

        });
    }

    /**
     * Undo delete prompt
     */
    private void deleteCancelSnackBar(final CourseV2 course) {
        course.setDisplayable(false);
        mCourseViewV2.resetView();
        Snackbar.make(mMMonthTextView, "Delete Success！☆\\(￣▽￣)/", Snackbar.LENGTH_LONG).setAction("撤销",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                switch (event) {
                    case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                    case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                    case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                        //delete
                        UserDataBase.getInstance(CourseActivity.this).getCourseDao().delete(course);
                        mCourseViewV2.resetView();
                        break;
                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                        //cancel
                        course.setDisplayable(true);
                        mCourseViewV2.resetView();
                        break;
                }
            }
        }).show();
    }

    private void updateView() {
        updateCoursePreference();
    }

    @SuppressLint("SetTextI18n")
    public void updateCoursePreference() {
        updateCurrentWeek();
        mMMonthTextView.setText(mCurrentMonth + "\nmonth");

        //get id
//        long currentCsNameId = Preferences.getLong(
//                getString(R.string.app_preference_current_cs_name_id), 0L);

//        mPresenter.updateCourseViewData(currentCsNameId);
        //Re-query data
        UserDataBase.getInstance(CourseActivity.this).getCourseDao().getAllUsers(mIntentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<CourseV2>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CourseV2> list) {
                        //Query result
                        if (list.size() != 0) {
                            mCourseViewV2.clear();
                            for (CourseV2 course : list) {
                                if (course.getCouColor() == null || course.getCouColor() == -1) {
                                    course.setCouColor(Utils.getRandomColor());
                                }
                                course.init();
                                mCourseViewV2.addCourse(course);
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

    @SuppressLint("SetTextI18n")
    private void updateCurrentWeek() {
        mCurrentWeekCount = AppUtils.getCurrentWeek(this);
        mTvWeekCount.setText( mCurrentWeekCount + "week");
        mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
    }

//    @Override
//    public void initFirstStart() {
//        boolean isFirst = Preferences.getBoolean(getString(R.string.app_preference_app_is_first_start), true);
//        if (!isFirst) {
//            return;
//        }
//        Preferences.putBoolean(getString(R.string.app_preference_app_is_first_start), false);
//
//        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();
//        CourseGroup defaultGroup = groupDao
//                .queryBuilder()
//                .where(CourseGroupDao.Properties.CgName.eq("默认课表"))
//                .unique();
//
//        long insert;
//        if (defaultGroup == null) {
//            insert = groupDao.insert(new CourseGroup(0L, "默认", ""));
//        } else {
//            insert = defaultGroup.getCgId();
//        }
//
//        //migrate old data
//        AppUtils.copyOldData(this);
//        Preferences.putLong(getString(R.string.app_preference_current_cs_name_id), insert);
//        showOnceSplash();
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mDialog != null) {
                    mDialog.dismiss();
                    Log.e("hao", "CourseActivity onKeyDown()");
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();
//        moveTaskToBack(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDialog != null) mDialog.dismiss();
        return super.onTouchEvent(event);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void courseChangeEvent(CourseDataChangeEvent event) {
//        //Update main interface
//        updateView();
//    }

}
