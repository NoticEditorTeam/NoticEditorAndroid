package com.noticeditorteam.noticeditorandroid.activities;

import android.app.Activity;
import android.content.Intent;
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
import com.noticeditorteam.noticeditorandroid.PreferencesRecentFilesService;
import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.RecentFilesService;
import com.noticeditorteam.noticeditorandroid.io.DocumentFormat;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String SAVE_RECENT = "recentnotes";
    private static final String CONFIG_RECENT = "RecentFiles";

    private static final String ROOT_BRANCH_TITLE = "root";

    private static final String ARG_NOTICE = "tree";
    private static final String ARG_FILE = "file";

    private static RecentFilesService filesService;

    private int FILE_CODE = 0;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filesService = PreferencesRecentFilesService.with(getSharedPreferences(CONFIG_RECENT, MODE_PRIVATE));
        if(savedInstanceState != null) {
            filesService.addAll(savedInstanceState.getStringArrayList(SAVE_RECENT));
        }
        rebuildRecentFilesList();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ListView list = findViewById(R.id.recentview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(filesService.getAllFiles()));
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
            if(uri != null) {
                openDocument(uri.getPath());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVE_RECENT, new ArrayList<>(filesService.getAllFiles()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        rebuildRecentFilesList();
        adapter.clear();
        adapter.addAll(filesService.getAllFiles());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void openDocument(String path) {
        File notice = new File(path);
        try {
            NoticeItem item = DocumentFormat.open(notice);
            Intent intent = new Intent(this, NoticeTreeActivity.class);
            intent.putExtra(ARG_NOTICE, item);
            intent.putExtra(ARG_FILE, path);
            filesService.addFile(notice.getAbsolutePath());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rebuildRecentFilesList() {
        List<String> toRemove = new ArrayList<>();
        List<String> toAdd = new ArrayList<>();
        for(String path : filesService.getAllFiles()) {
            File notice = new File(path);
            toAdd.add(path);
            if(!notice.exists()) toRemove.add(path);
        }
        filesService.addAll(toAdd);
        filesService.removeAll(toRemove);
    }
}
