package com.noticeditorteam.noticeditorandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import us.feras.mdv.MarkdownView;

public class ViewNoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice);
        MarkdownView view = (MarkdownView) findViewById(R.id.markdownView);
        String content = getIntent().getStringExtra("content");
        view.loadMarkdown(content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.viewToolbar);
        setSupportActionBar(toolbar);
    }

}
