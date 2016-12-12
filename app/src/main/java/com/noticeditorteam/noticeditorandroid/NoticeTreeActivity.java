package com.noticeditorteam.noticeditorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

public class NoticeTreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_tree);
        NoticeItem tree = getIntent().getParcelableExtra("tree");
        String path = getIntent().getStringExtra("file");
        ListView list = (ListView) findViewById(R.id.noticeview);
        ArrayAdapter<NoticeItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tree.getChildren());
        list.setAdapter(adapter);
        list.setOnItemClickListener((AdapterView<?> parent, View itemClicked, int position, long id) -> {
            NoticeItem newitem = (NoticeItem)adapter.getItem(position);
            Intent intent;
            if(newitem.isBranch())
                intent = new Intent(this, NoticeTreeActivity.class);
            else
                intent = new Intent(this, EditNoticeActivity.class);
            intent.putExtra("tree", newitem);
            intent.putExtra("file", path);
            startActivity(intent);
        });
    }
}
