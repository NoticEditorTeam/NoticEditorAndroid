package com.noticeditorteam.noticeditorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.noticeditorteam.noticeditorandroid.io.DocumentFormat;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategyHolder;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;
import java.util.ArrayDeque;

public class NoticeTreeActivity extends AppCompatActivity {

    private NoticeItem current;
    private ArrayAdapter<NoticeItem> adapter;
    private ArrayDeque<NoticeItem> pathlist = new ArrayDeque<>();
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_tree);
        NoticeItem tree = getIntent().getParcelableExtra("tree");
        current = tree;
        if(pathlist.isEmpty()) pathlist.addLast(current);
        path = getIntent().getStringExtra("file");
        ListView list = (ListView) findViewById(R.id.noticeview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, current.getChildren());
        list.setAdapter(adapter);
        list.setOnItemClickListener((AdapterView<?> parent, View itemClicked, int position, long id) -> {
            current = (NoticeItem)adapter.getItem(position);
            if(current.isBranch()) {
                pathlist.addLast(current);
                adapter.clear();
                adapter.addAll(current.getChildren());
                adapter.notifyDataSetChanged();
            }
            else {
                Intent intent = new Intent(this, EditNoticeActivity.class);
                intent.putExtra("tree", current);
                intent.putExtra("index", position);
                startActivityForResult(intent, 1);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tree, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.newbranchitem:
                break;
            case R.id.newnoticeitem:
                break;
            case R.id.saveitem:
                NoticeItem root = pathlist.getFirst();
                DocumentFormat.save(root, new File(path), ExportStrategyHolder.ZIP);
                break;
            case R.id.saveasitem:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(pathlist.size() > 1) {
            NoticeItem last = pathlist.peekLast();
            if(last.equals(current)) pathlist.removeLast();
            current = pathlist.getLast();
            adapter.clear();
            adapter.addAll(current.getChildren());
            adapter.notifyDataSetChanged();
        }
        else finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        NoticeItem oldcurrent = current;
        current = pathlist.getLast();
        NoticeItem notice = data.getParcelableExtra("tree");
        int ind = current.getChildren().indexOf(oldcurrent);
        current.getChildren().remove(oldcurrent);
        current.getChildren().add(ind, notice);
        System.out.println(3);
    }
}
