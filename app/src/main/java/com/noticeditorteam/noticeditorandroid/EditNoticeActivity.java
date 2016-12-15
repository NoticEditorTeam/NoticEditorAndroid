package com.noticeditorteam.noticeditorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.noticeditorteam.noticeditorandroid.io.DocumentFormat;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategy;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategyHolder;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;

public class EditNoticeActivity extends AppCompatActivity {

    private String path;
    private NoticeItem notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notice);
        NoticeItem notice = getIntent().getParcelableExtra("tree");
        path = getIntent().getStringExtra("file");
        EditText noticeText = (EditText)findViewById(R.id.editNotice);
        noticeText.setText(notice.getContent());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        notice = getIntent().getParcelableExtra("tree");
        switch(id) {
            case R.id.viewitem:
                Intent intent = new Intent(this, ViewNoticeActivity.class);
                intent.putExtra("content", notice.getContent());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("tree", notice);
        setResult(1, returnIntent);
        super.onBackPressed();
    }
}
