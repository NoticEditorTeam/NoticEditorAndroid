package com.noticeditorteam.noticeditorandroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.noticeditorteam.noticeditorandroid.io.DocumentFormat;
import com.noticeditorteam.noticeditorandroid.io.IOUtil;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.beans.IndexedPropertyChangeEvent;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private int FILE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
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
                intent.putExtra("tree", new NoticeItem("root"));
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
            File notice = new File(uri.getPath());
            try {
                NoticeItem item = DocumentFormat.open(notice);
                Intent intent = new Intent(this, NoticeTreeActivity.class);
                intent.putExtra("tree", item);
                intent.putExtra("file", uri.getPath());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
