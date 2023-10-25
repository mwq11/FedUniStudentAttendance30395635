package au.edu.federation.itech3107.studentattendance30395635.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.Constant;
import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.activity.CourseActivity;
import au.edu.federation.itech3107.studentattendance30395635.adapter.CourseAdapter;
import au.edu.federation.itech3107.studentattendance30395635.databinding.FragmentCourseBinding;
import au.edu.federation.itech3107.studentattendance30395635.room.CourseGroupBean;
import au.edu.federation.itech3107.studentattendance30395635.room.UserDataBase;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Curriculum management
 */
public class CourserFragment extends AppCompatDialogFragment{

   private FragmentCourseBinding bind;
   private CourseAdapter mAdapter;
    private String headerText;

    @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      bind = FragmentCourseBinding.inflate(inflater,container,false);
      initView();
      return bind.getRoot();
   }

   private void initView() {
      bind.add.setOnClickListener(v -> {
          showDataPickDialog();
      });

      bind.rv.setLayoutManager(new LinearLayoutManager(getContext()));
      mAdapter = new CourseAdapter(R.layout.item_course, new ArrayList<>());
      mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
         @Override
         public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
             CourseGroupBean courseGroupBean = mAdapter.getData().get(position);
             Constant.selectId = courseGroupBean.getId();
             startActivity(new Intent(getContext(), CourseActivity.class)
                     .putExtra("id",courseGroupBean.getId()));
         }
      });
      bind.rv.setAdapter(mAdapter);
   }

   @Override
   public void onResume() {
      super.onResume();
      UserDataBase.getInstance(getContext()).getCourseGroupDao().getAllUsers()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new MaybeObserver<List<CourseGroupBean>>() {
                 @Override
                 public void onSubscribe(Disposable d) {

                 }

                 @Override
                 public void onSuccess(List<CourseGroupBean> list) {
                    //Query result
                    if (list.size() != 0) {
                       mAdapter.setNewData(list);
                       for(int i = 0; i < list.size(); i++) {
                           Log.e("hao", "list: "+list.get(i).toString());
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

   public void showDataPickDialog(){
      //            Calendar calendar = Calendar.getInstance();
//            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//               @Override
//               public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                  String desc = String.format("选中日期%d年%d月%d日",year,month+1,day);
//
//               }
//            },
//                    calendar.get(Calendar.YEAR),
//                    calendar.get(Calendar.MARCH),
//                    calendar.get(Calendar.DAY_OF_MONTH));
//            dialog.show();
      long today = MaterialDatePicker.todayInUtcMilliseconds();
      Pair<Long, Long> todayPair = new Pair<>(today, today);
      MaterialDatePicker.Builder<Pair<Long, Long>> builder =
              MaterialDatePicker.Builder.dateRangePicker();
      builder.setSelection(todayPair);
      CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();;
      try {
         builder.setCalendarConstraints(constraintsBuilder.build());
         MaterialDatePicker<?> picker = builder.build();
         picker.addOnPositiveButtonClickListener(selection -> {
             headerText = picker.getHeaderText();
             CourseGroupBean bean = new CourseGroupBean();
             bean.setName(headerText);
             Log.e("hao", "headerText: "+headerText);
             UserDataBase.getInstance(getContext()).getCourseGroupDao().insert(bean);
             UserDataBase.getInstance(getContext()).getCourseGroupDao().getAllUsers()
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(new MaybeObserver<List<CourseGroupBean>>() {
                         @Override
                         public void onSubscribe(Disposable d) {

                         }

                         @Override
                         public void onSuccess(List<CourseGroupBean> list) {
                             //Query result
                             if (list.size() != 0) {
                                 mAdapter.setNewData(list);
                                 for(int i = 0; i < list.size(); i++) {
                                     Log.e("hao", "list: "+list.get(i).toString());
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
         });
         picker.show(getChildFragmentManager(), picker.toString());

      } catch (IllegalArgumentException e) {
      }

   }
}
