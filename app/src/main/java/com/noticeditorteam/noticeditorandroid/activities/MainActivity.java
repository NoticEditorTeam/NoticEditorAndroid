package com.noticeditorteam.noticeditorandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.io.DocumentFormat;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String RES_RECENT = "recentnotes";
    private static final String SAVE_RECENT = "recentnotes";
    private static final String ROOT_BRANCH_TITLE = "root";
    private static final String ARG_NOTICE = "tree";
    private static final String ARG_FILE = "file";

    private int FILE_CODE = 0;

    private static ArrayList<String> recentFiles = new ArrayList<>();
    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        recentFiles.clear();
        recentFiles.addAll(preferences.getStringSet(RES_RECENT, new HashSet<>()));
        if(savedInstanceState != null) {
            recentFiles = savedInstanceState.getStringArrayList(SAVE_RECENT);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView list = (ListView) findViewById(R.id.recentview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentFiles);
        list.setAdapter(adapter);
        list.setOnItemClickListener((AdapterView<?> parent, View itemClicked, int position, long id)
                -> openDocument(adapter.getItem(position)));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.newitem:
                Intent intent = new Intent(this, NoticeTreeActivity.class);
                intent.putExtra(ARG_NOTICE, new NoticeItem(ROOT_BRANCH_TITLE));
                startActivity(intent);
                break;
            case R.id.openitem:
                Intent intent1 = new Intent(this, FilePickerActivity.class);
                intent1.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                intent1.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                intent1.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                intent1.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(intent1, FILE_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            openDocument(uri.getPath());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVE_RECENT, recentFiles);
    }

    @Override
    protected void onDestroy() {
        Set<String> set = new HashSet<>();
        set.addAll(recentFiles);
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putStringSet(RES_RECENT, set);
        editor.apply();
        super.onDestroy();
    }

    private void openDocument(String path) {
        File notice = new File(path);
        try {
            NoticeItem item = DocumentFormat.open(notice);
            recentFiles.remove(path);
            recentFiles.add(path);
            adapter.remove(path);
            adapter.add(path);
            Intent intent = new Intent(this, NoticeTreeActivity.class);
            intent.putExtra(ARG_NOTICE, item);
            intent.putExtra(ARG_FILE, path);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
