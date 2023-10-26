package au.edu.federation.itech3107.studentattendance30395635.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.activity.StudentActivity;
import au.edu.federation.itech3107.studentattendance30395635.adapter.ClassAdapter;
import au.edu.federation.itech3107.studentattendance30395635.databinding.FragmentMineBinding;
import au.edu.federation.itech3107.studentattendance30395635.room.ClassBean;
import au.edu.federation.itech3107.studentattendance30395635.room.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395635.util.DialogUtil;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Student management
 */
public class MineFragment extends AppCompatDialogFragment {

   private FragmentMineBinding bind;
   private ClassAdapter mAdapter;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      bind = FragmentMineBinding.inflate(inflater,container,false);
      initView();
      return bind.getRoot();
   }

   private void initView() {
      bind.add.setOnClickListener(v -> {
         DialogUtil.showDialog(getContext(), new DialogUtil.onClickConfirmListener() {
            @Override
            public void onClick(String text) {
               ClassBean bean = new ClassBean();
               bean.setName(text);
               UserDataBase.getInstance(getContext()).getClassDao().insert(bean);
               Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
               searchData();
            }
         });
      });

      bind.rv.setLayoutManager(new LinearLayoutManager(getContext()));
      mAdapter = new ClassAdapter(R.layout.item_course, new ArrayList<>());
      mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
         @Override
         public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            ClassBean bean = mAdapter.getData().get(position);
            startActivity(new Intent(getContext(), StudentActivity.class)
                    .putExtra("id",bean.getId()));
         }
      });
      bind.rv.setAdapter(mAdapter);
   }

   @Override
   public void onResume() {
      super.onResume();
      searchData();
   }

   private void searchData(){
      UserDataBase.getInstance(getContext()).getClassDao().getAllUsers()
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
}
