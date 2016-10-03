package com.navercorp.recruit.kimkihwan.stepcounter.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.navercorp.recruit.kimkihwan.stepcounter.R;
import com.navercorp.recruit.kimkihwan.stepcounter.adapter.SectionsPagerAdapter;
import com.navercorp.recruit.kimkihwan.stepcounter.fragment.DashboardFragment;
import com.navercorp.recruit.kimkihwan.stepcounter.fragment.HistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class TabbedActivity extends AppCompatActivity {

    private SectionsPagerAdapter pagerAdapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getSections());

        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    private List<Fragment> getSections() {
        List<Fragment> sections = new ArrayList<>();
        sections.add(DashboardFragment.newInstance(0, getString(R.string.section_dashboard_title)));
        sections.add(HistoryFragment.newInstance(1, getString(R.string.section_history_title)));
        return sections;
    }





}
