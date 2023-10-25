package au.edu.federation.itech3107.studentattendance30395635.adapter;


import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.room.ClassBean;
import au.edu.federation.itech3107.studentattendance30395635.room.StudentBean;
import au.edu.federation.itech3107.studentattendance30395635.room.UserDataBase;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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
        UserDataBase.getInstance(mContext).getStudentDao().getAllUsers(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<StudentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<StudentBean> list) {
                        //Query result
                        if (list.size() != 0) {
                            mAdapter.setNewData(list);
                            for (StudentBean bean : list) {
                                Log.e("hao", "学生数据:" + bean.toString());
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
