package com.noticeditorteam.noticeditorandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditNoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notice);
        String content = getIntent().getStringExtra("content");
        EditText noticeText = (EditText)findViewById(R.id.editNotice);
        noticeText.setText(content);
    }
}
