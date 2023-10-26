package au.edu.federation.itech3107.studentattendance30395635.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.Constant;
import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.room.CourseV2;
import au.edu.federation.itech3107.studentattendance30395635.util.DialogHelper;
import au.edu.federation.itech3107.studentattendance30395635.util.DialogListener;

/**
 * 显示详细信息
 */

public class PopupWindowDialog implements View.OnClickListener {
    private int mWeek;
    private int mNodeStart;
    private int mNodeEnd;

    private List<View> mAllWeekTextView;
    private View mBtnWeekAll;
    private View mBtnWeekSingle;
    private View mBtnWeekDouble;
    private static final Integer MAX_WEEK = 25;
    private TextInputEditText mEtLocation;


    public interface SelectTimeCallback {
        void onSelected(CourseV2 course);
    }

    public void showSelectTimeDialog(Activity activity, final CourseV2 course, final SelectTimeCallback callback) {
        mWeek = course.getCouWeek();
        mNodeStart = course.getCouStartNode();
        mNodeEnd = course.getCouStartNode() + course.getCouNodeCount() - 1;

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_week_and_node, null);

        final ArrayList<String> weeks = new ArrayList<>();
        final ArrayList<String> nodes = new ArrayList<>();

        WheelView wvWeek = (WheelView) view.findViewById(R.id.wv_week);
        WheelView wvStart = (WheelView) view.findViewById(R.id.wv_start_node);
        final WheelView wvEnd = (WheelView) view.findViewById(R.id.wv_end_node);

        mEtLocation = view.findViewById(R.id.et_location);
        mEtLocation.setText(course.getCouLocation());


        initWeekSelect(view, course.getShowIndexes());

        for (int i = 1; i <= 7; i++) {
            weeks.add(Constant.WEEK[i]);
        }

        int maxNode = 16;
        for (int i = 1; i <= maxNode; i++) {
            nodes.add("Section " + i );
        }

        wvWeek.setItems(weeks);
        wvStart.setItems(nodes);
        wvEnd.setItems(nodes);

        wvWeek.setSeletion(mWeek - 1);
        wvStart.setSeletion(mNodeStart - 1);
        wvEnd.setSeletion(mNodeEnd - 1);

        wvWeek.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mWeek = selectedIndex;
            }
        });

        wvStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mNodeStart = selectedIndex;
                if (mNodeStart > mNodeEnd) {
                    wvEnd.setSeletion(mNodeStart - 1);
                    return;
                }
            }
        });

        wvEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mNodeEnd = selectedIndex;

                if (mNodeStart > mNodeEnd) {
                    wvEnd.setSeletion(mNodeStart - 1);
                }
            }
        });

        show(activity, course, callback, view);
    }

    private void show(Activity activity, final CourseV2 course, final SelectTimeCallback callback, View view) {
        DialogHelper helper = new DialogHelper();
        helper.showCustomDialog(activity, view, null, new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                dialog.dismiss();

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mAllWeekTextView.size(); i++) {
                    if (mAllWeekTextView.get(i).isSelected()) {
                        builder.append(i + 1).append(",");
                    }
                }
                String allWeeks = builder.toString();
                if (allWeeks.length() > 0) {
                    allWeeks = allWeeks.substring(0, allWeeks.length() - 1);
                }
                course.setCouLocation(mEtLocation.getText().toString().trim());
                course.setCouAllWeek(allWeeks);
                course.setCouWeek(mWeek);
                course.setCouStartNode(mNodeStart);
                course.setCouNodeCount(mNodeEnd - mNodeStart + 1);
                course.init();
                callback.onSelected(course);
            }
        });
    }


    private void initWeekSelect(View view, List<Integer> selectWeeks) {
        View.OnClickListener weekTextViewCLickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
            }
        };

        mBtnWeekAll = view.findViewById(R.id.btn_week_all);
        mBtnWeekSingle = view.findViewById(R.id.btn_week_single);
        mBtnWeekDouble = view.findViewById(R.id.btn_week_double);
        ViewGroup layoutWeekContainer = view.findViewById(R.id.layout_week_container);

        mBtnWeekAll.setSelected(true);
        mBtnWeekAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnWeekAll.setSelected(true);
                mBtnWeekSingle.setSelected(false);
                mBtnWeekDouble.setSelected(false);

                for (View view : mAllWeekTextView) {
                    view.setSelected(true);
                }
            }
        });
        mBtnWeekSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnWeekAll.setSelected(false);
                mBtnWeekSingle.setSelected(true);
                mBtnWeekDouble.setSelected(false);

                for (int i = 0; i < mAllWeekTextView.size(); i++) {
                    mAllWeekTextView.get(i).setSelected(i % 2 == 0);
                }
            }
        });
        mBtnWeekDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnWeekAll.setSelected(false);
                mBtnWeekSingle.setSelected(false);
                mBtnWeekDouble.setSelected(true);

                for (int i = 0; i < mAllWeekTextView.size(); i++) {
                    mAllWeekTextView.get(i).setSelected(i % 2 == 1);
                }
            }
        });

        mAllWeekTextView = getAllChildViews(layoutWeekContainer);

        if (selectWeeks == null || selectWeeks.isEmpty()) {
            for (View view1 : mAllWeekTextView) {
                view1.setSelected(true);
                view1.setOnClickListener(weekTextViewCLickListener);
            }
        } else {
            for (View view1 : mAllWeekTextView) {
                view1.setOnClickListener(weekTextViewCLickListener);
            }

            for (Integer selectWeek : selectWeeks) {
                if (selectWeek <= MAX_WEEK && selectWeek >= 1) {
                    mAllWeekTextView.get(selectWeek - 1).setSelected(true);
                } else {
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    private List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                //allchildren.add(viewchild);
                if (!(viewchild instanceof ViewGroup)) {
                    allchildren.add(viewchild);
                }
                //再次 调用本身（递归）
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }
}
