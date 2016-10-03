package com.navercorp.recruit.kimkihwan.stepcounter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.navercorp.recruit.kimkihwan.stepcounter.fragment.SectionFragment;

import java.util.List;

/**
 * Created by jamie on 10/2/16.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> sections;

    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> sections) {
        super(fm);
        this.sections = sections;
    }

    @Override
    public Fragment getItem(int position) {
        return sections.get(position);
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sections.get(position).getArguments().getString(SectionFragment.ARG_SECTION_TITLE);
    }
}
