package au.edu.federation.itech3107.studentattendance30395635.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.R;
import au.edu.federation.itech3107.studentattendance30395635.fragment.CourserFragment;
import au.edu.federation.itech3107.studentattendance30395635.fragment.MineFragment;
import au.edu.federation.itech3107.studentattendance30395635.util.TabViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

//    private ActivityMainBinding inflate;
//    private
    private List<AppCompatDialogFragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        inflate = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(inflate.getRoot());
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(new CourserFragment());
        mFragments.add(new MineFragment());
        ViewPager vp = findViewById(R.id.vp);
        TabLayout tab = findViewById(R.id.tab);
        String[] mTitle = new String[]{getResources().getString(R.string.course), getResources().getString(R.string.student)};
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitle);
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
        tab.getTabAt(0).setText(mTitle[0]).setIcon(R.mipmap.ic_tab_co);
        tab.getTabAt(1).setText(mTitle[1]).setIcon(R.mipmap.ic_tab_class);

        int type = getIntent().getIntExtra("type", 0);
        vp.setCurrentItem(type);
    }
}