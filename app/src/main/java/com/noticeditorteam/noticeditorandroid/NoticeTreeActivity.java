package com.noticeditorteam.noticeditorandroid;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.noticeditorteam.noticeditorandroid.io.DocumentFormat;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategyHolder;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class NoticeTreeActivity extends AppCompatActivity implements RenameDialogFragment.RenameDialogListener {

    private final int EDIT_NOTICE_REQUEST = 1;
    private final int SELECT_FILE_REQUEST = 2;

    private NoticeItem current;
    private ArrayAdapter<NoticeItem> adapter;
    private ArrayDeque<NoticeItem> pathlist = new ArrayDeque<>();
    private String path, savepath;

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
        list.setLongClickable(true);
        list.setOnItemClickListener((AdapterView<?> parent, View itemClicked, int position, long id) -> {
            current = adapter.getItem(position);
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
        registerForContextMenu(list);
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
                NoticeItem newBranch = new NoticeItem("New branch");
                current.getChildren().add(newBranch);
                adapter.add(newBranch);
                adapter.notifyDataSetChanged();
                break;
            case R.id.newnoticeitem:
                NoticeItem newNotice = new NoticeItem("New notice", "Enter your notice here");
                current.getChildren().add(newNotice);
                adapter.add(newNotice);
                adapter.notifyDataSetChanged();
                break;
            case R.id.saveitem:
                if(path != null) {
                    NoticeItem root = pathlist.getFirst();
                    DocumentFormat.save(root, new File(path), ExportStrategyHolder.ZIP);
                }
                else {
                    showSaveDialog();
                    path = savepath;
                }
                break;
            case R.id.saveasitem:
                showSaveDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.noticeview)
            getMenuInflater().inflate(R.menu.context_menu_bnotice, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NoticeItem changingItem = adapter.getItem(info.position);
        switch (id) {
            case R.id.renameitem:
                DialogFragment fragment = new RenameDialogFragment();
                Bundle args = new Bundle();
                args.putParcelable("tree", changingItem);
                fragment.setArguments(args);
                fragment.show(getFragmentManager(), "missiles");
                break;
            case R.id.deletebnoticeitem:
                current.getChildren().remove(changingItem);
                adapter.remove(changingItem);
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showSaveDialog() {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_NEW_FILE);
        intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(intent, SELECT_FILE_REQUEST);
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
        switch(requestCode) {
            case EDIT_NOTICE_REQUEST:
                NoticeItem oldcurrent = current;
                current = pathlist.getLast();
                NoticeItem notice = data.getParcelableExtra("tree");
                int ind = current.getChildren().indexOf(oldcurrent);
                current.getChildren().set(ind, notice);
                break;
            case SELECT_FILE_REQUEST:
                try {
                    savepath = data.getData().getPath();
                    NoticeItem root = pathlist.getFirst();
                    DocumentFormat.save(root, new File(savepath), ExportStrategyHolder.ZIP);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(RenameDialogFragment dialog) {
        Bundle args = dialog.getArguments();
        NoticeItem renamingItem = args.getParcelable("tree");
        int ind = current.getChildren().indexOf(renamingItem);
        renamingItem.setTitle(dialog.getNoticeName().getText().toString());
        current.getChildren().set(ind, renamingItem);
        adapter.clear();
        adapter.addAll(current.getChildren());
        adapter.notifyDataSetChanged();
    }
}
