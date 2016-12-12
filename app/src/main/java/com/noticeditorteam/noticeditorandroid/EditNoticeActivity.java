package com.noticeditorteam.noticeditorandroid;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notice);
        NoticeItem notice = getIntent().getParcelableExtra("tree");
        path = getIntent().getStringExtra("file");
        EditText noticeText = (EditText)findViewById(R.id.editNotice);
        noticeText.setText(notice.getContent());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
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
        switch(id) {
            case R.id.viewitem:
                break;
            case R.id.saveitem:
                NoticeItem notice = getIntent().getParcelableExtra("tree");
                EditText noticeText = (EditText)findViewById(R.id.editNotice);
                notice.changeContent(noticeText.getText().toString());
                File file = new File(path);
                DocumentFormat.save(notice, file, ExportStrategyHolder.ZIP);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
