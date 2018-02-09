package com.noticeditorteam.noticeditorandroid.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.noticeditorteam.noticeditorandroid.fragments.EditNoticeFragment;
import com.noticeditorteam.noticeditorandroid.fragments.PreviewNoticeFragment;
import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

public class NoticeWorkActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 2;
    private static final String ARG_TREE = "tree";
    private static final String RESULT_TREE = "tree";

    public NoticeItem getNotice() {
        return notice;
    }

    private NoticeItem notice;
    private EditNoticeFragment editFragment;
    private PreviewNoticeFragment previewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notice = getIntent().getParcelableExtra(ARG_TREE);
        if(savedInstanceState != null) notice = savedInstanceState.getParcelable(ARG_TREE);
        editFragment = EditNoticeFragment.newInstance();
        previewFragment = PreviewNoticeFragment.newInstance();
        setContentView(R.layout.activity_notice_work);
        ViewPager pager = findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TREE, notice);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent returnIntent = new Intent(this, NoticeTreeActivity.class);
        returnIntent.putExtra(RESULT_TREE, notice);
        setResult(1, returnIntent);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? editFragment
                                 : previewFragment;
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