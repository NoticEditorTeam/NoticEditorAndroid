package com.noticeditorteam.noticeditorandroid;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

public class NoticeWorkActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 2;
    private static final String ARG_TREE = "tree";
    private static final String RESULT_TREE = "tree";

    private NoticeItem notice;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_work);
        notice = getIntent().getParcelableExtra(ARG_TREE);
        if(savedInstanceState != null) notice = savedInstanceState.getParcelable(ARG_TREE);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TREE, notice);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent(this, NoticeTreeActivity.class);
        returnIntent.putExtra(RESULT_TREE, notice);
        setResult(1, returnIntent);
        super.onBackPressed();
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? EditNoticeFragment.newInstance(notice)
                                 : PreviewNoticeFragment.newInstance(notice);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Edit" : "Preview";
        }

    }
}