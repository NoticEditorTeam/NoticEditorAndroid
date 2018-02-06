package com.noticeditorteam.noticeditorandroid.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.noticeditorteam.noticeditorandroid.MultiChoiceHelper;
import com.noticeditorteam.noticeditorandroid.NoticeTreeAdapter;
import com.noticeditorteam.noticeditorandroid.PreferencesRecentFilesService;
import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.RecentFilesService;
import com.noticeditorteam.noticeditorandroid.fragments.FileTypeFragment;
import com.noticeditorteam.noticeditorandroid.fragments.RenameDialogFragment;
import com.noticeditorteam.noticeditorandroid.io.DocumentFormat;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategy;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategyHolder;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class NoticeTreeActivity extends AppCompatActivity implements
        RenameDialogFragment.RenameDialogListener, FileTypeFragment.OnFragmentInteractionListener {

    private static final int EDIT_NOTICE_REQUEST = 1;
    private static final int SELECT_FILE_REQUEST = 2;

    private static final String ARG_TREE = "tree";
    private static final String ARG_FILE = "file";
    private static final String ARG_NAME = "name";
    private static final String ARG_POSITION = "position";

    private static final String SAVE_TREE = "tree";
    private static final String SAVE_FILE = "file";

    private static final String RESULT_TREE = "tree";

    private static final String CONFIG_RECENT = "RecentFiles";

    private static final AppCompatDialogFragment renameDialogFragment = new RenameDialogFragment();

    private NoticeItem current;
    private NoticeItem savingItem;
    private NoticeTreeAdapter noticeTreeAdapter;
    private ArrayDeque<NoticeItem> pathlist = new ArrayDeque<>();
    private String path, savepath;
    private ExportStrategy currentExportStrategy = ExportStrategyHolder.ZIP;
    private RecentFilesService service;
    private boolean isMenuShown = false;
    private FloatingActionButton addNoticeButton, addBranchButton;
    private Animation showNoticeButton, hideNoticeButton, showBranchButton, hideBranchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_tree);
        current = getIntent().getParcelableExtra(ARG_TREE);
        path = getIntent().getStringExtra(ARG_FILE);
        service = PreferencesRecentFilesService.with(getSharedPreferences(CONFIG_RECENT, MODE_PRIVATE));
        if(savedInstanceState != null) {
            current = savedInstanceState.getParcelable(SAVE_TREE);
            path = savedInstanceState.getString(SAVE_FILE);
        }
        if(current == null) {
            current = openDocument(getIntent().getData());
            path = getIntent().getData().getPath();
        }
        if(pathlist.isEmpty()) pathlist.addLast(current);
        FloatingActionButton addNoticeMenu = findViewById(R.id.add_notice_menu);
        addNoticeButton = findViewById(R.id.add_notice_button);
        addBranchButton = findViewById(R.id.add_branch_button);
        showNoticeButton = AnimationUtils.loadAnimation(getApplication(), R.anim.show_notice_button);
        hideNoticeButton = AnimationUtils.loadAnimation(getApplication(), R.anim.hide_notice_button);
        showBranchButton = AnimationUtils.loadAnimation(getApplication(), R.anim.show_branch_button);
        hideBranchButton = AnimationUtils.loadAnimation(getApplication(), R.anim.hide_branch_button);
        addNoticeMenu.setOnClickListener((View v) -> {
            if(!isMenuShown) {
                showMenu();
            }
            else {
                hideMenu();
            }
        });
        addNoticeButton.setOnClickListener((View v) -> {
            NoticeItem newNotice = new NoticeItem("New notice", "Enter your notice here");
            current.getChildren().add(newNotice);
            noticeTreeAdapter.add(newNotice);
            noticeTreeAdapter.notifyItemInserted(noticeTreeAdapter.getItemCount() - 1);
        });
        addBranchButton.setOnClickListener((View v) -> {
            NoticeItem newBranch = new NoticeItem("New branch");
            current.getChildren().add(newBranch);
            noticeTreeAdapter.add(newBranch);
            noticeTreeAdapter.notifyItemInserted(noticeTreeAdapter.getItemCount() - 1);
        });
        RecyclerView recview = findViewById(R.id.noticeview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recview.setLayoutManager(mLayoutManager);
        recview.setItemAnimator(new DefaultItemAnimator());
        recview.setLongClickable(true);
        recview.addItemDecoration(new DividerItemDecoration(
                recview.getContext(),
                mLayoutManager.getOrientation()
        ));
        recview.addOnScrollListener(new OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy != 0 && addNoticeMenu.isShown())
                    addNoticeMenu.hide();
                if(dy != 0 && isMenuShown) {
                    hideMenu();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    addNoticeMenu.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        NoticeTreeAdapter.OnNoticeClickListener listener = new NoticeTreeAdapter.OnNoticeClickListener() {
            @Override
            public void onClick(View v) {
                NoticeItem currentItem = getCurrentNotice();
                current = currentItem;
                if(currentItem.isBranch()) {
                    pathlist.addLast(currentItem);
                    noticeTreeAdapter.clear();
                    noticeTreeAdapter.addAll(currentItem.getChildren());
                    noticeTreeAdapter.notifyDataSetChanged();
                }
                else {
                    Intent intent = new Intent(getContext(), NoticeWorkActivity.class);
                    intent.putExtra(ARG_TREE, current);
                    startActivityForResult(intent, 1);
                }
            }
        };
        listener.setContext(this);
        noticeTreeAdapter = new NoticeTreeAdapter(this,
                new ArrayList<>(current.getChildren()), listener);
        MultiChoiceHelper.MultiChoiceModeListener mListener = new MultiChoiceHelper.MultiChoiceModeListener() {
            private Menu menu = null;

            @Override
            public void onItemCheckedStateChanged(android.support.v7.view.ActionMode mode,
                                                  int position, long id, boolean checked) {
                NoticeTreeAdapter.ViewHolder itemHolder =
                        (NoticeTreeAdapter.ViewHolder) recview.findViewHolderForAdapterPosition(position);
                if(itemHolder != null) {
                    if(checked) {
                        itemHolder.getRelativeLayout().setBackgroundColor(fetchSelectionColor());
                    }
                    else {
                        itemHolder.getRelativeLayout().setBackgroundColor(Color.WHITE);
                    }
                }
                if(noticeTreeAdapter.getHelper().getCheckedItemCount() > 1) {
                    menu.findItem(R.id.renameitem).setVisible(false);
                    menu.findItem(R.id.savenoticeitem).setVisible(false);
                }
                else {
                    menu.findItem(R.id.renameitem).setVisible(true);
                    menu.findItem(R.id.savenoticeitem).setVisible(true);
                }
            }

            @Override
            public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_bnotice, menu);
                this.menu = menu;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                this.menu = menu;
                return true;
            }

            @Override
            public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
                int id = item.getItemId();
                NoticeItem changingItem;
                SparseBooleanArray positions = noticeTreeAdapter.getHelper().getCheckedItemPositions();
                switch (id) {
                    case R.id.renameitem:
                        Bundle args = new Bundle();
                        for(int i = positions.size() - 1; i >= 0; --i) {
                            int key = positions.keyAt(i);
                            if (positions.get(key)) {
                                args.putInt(ARG_POSITION, key);
                                changingItem = noticeTreeAdapter.getItem(key);
                                assert changingItem != null;
                                args.putString(ARG_NAME, changingItem.getTitle());
                                renameDialogFragment.setArguments(args);
                                noticeTreeAdapter.getHelper().setItemChecked(key, false, true);
                                if(!renameDialogFragment.isAdded()) {
                                    renameDialogFragment.show(getSupportFragmentManager(), "missiles");
                                }
                            }
                        }
                        break;
                    case R.id.deletebnoticeitem:
                        for(int i = positions.size() - 1; i >= 0; --i) {
                            int key = positions.keyAt(i);
                            if(positions.get(key)) {
                                changingItem = noticeTreeAdapter.getItem(key);
                                current.getChildren().remove(changingItem);
                                noticeTreeAdapter.remove(changingItem);
                                noticeTreeAdapter.notifyItemRemoved(key);
                            }
                        }
                        noticeTreeAdapter.getHelper().clearChoices();
                        break;
                    case R.id.savenoticeitem:
                        for(int i = positions.size() - 1; i >= 0; --i) {
                            int key = positions.keyAt(i);
                            if (positions.get(key)) {
                                changingItem = noticeTreeAdapter.getItem(key);
                                savingItem = new NoticeItem("Root");
                                savingItem.addChild(changingItem);
                                noticeTreeAdapter.getHelper().setItemChecked(key, false, true);
                                showSaveDialog();
                            }
                        }
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
                menu = null;
            }
        };
        noticeTreeAdapter.setModeListener(mListener);
        recview.setAdapter(noticeTreeAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
                noticeTreeAdapter.add(newBranch);
                noticeTreeAdapter.notifyDataSetChanged();
                break;
            case R.id.newnoticeitem:
                NoticeItem newNotice = new NoticeItem("New notice", "Enter your notice here");
                current.getChildren().add(newNotice);
                noticeTreeAdapter.add(newNotice);
                noticeTreeAdapter.notifyDataSetChanged();
                break;
            case R.id.saveitem:
                if(path != null) {
                    exportDocument(pathlist.getFirst(), path, currentExportStrategy);
                }
                else {
                    savingItem = pathlist.getFirst();
                    showSaveDialog();
                    path = savepath;
                }
                break;
            case R.id.saveasitem:
                savingItem = pathlist.getFirst();
                showSaveDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportDocument(NoticeItem root, String path, ExportStrategy currentExportStrategy) {
        DocumentFormat.save(root, new File(path), currentExportStrategy);
        service.addFile(path);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor;
        String[] proj = { MediaStore.Images.Media.DATA };
        cursor = getContentResolver().query(uri, proj, null, null, null);
        if(cursor == null) {
            return uri.getPath();
        }
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String result = cursor.getString(idx);
            cursor.close();
            return result;
        }
    }

    private NoticeItem openDocument(Uri uri) {
        String path = getRealPathFromURI(uri);
        File notice = new File(path);
        try {
            NoticeItem item = DocumentFormat.open(notice);
            service.addFile(path);
            return item;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showSaveDialog() {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
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
            noticeTreeAdapter.clear();
            noticeTreeAdapter.addAll(current.getChildren());
            noticeTreeAdapter.notifyDataSetChanged();
        }
        else finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case EDIT_NOTICE_REQUEST:
                NoticeItem oldcurrent = current;
                current = pathlist.getLast();
                NoticeItem notice = data.getParcelableExtra(RESULT_TREE);
                int ind = current.getChildren().indexOf(oldcurrent);
                current.getChildren().set(ind, notice);
                noticeTreeAdapter.set(ind, notice);
                noticeTreeAdapter.notifyItemChanged(ind);
                break;
            case SELECT_FILE_REQUEST:
                if((data != null) && (data.getData() != null)) {
                    savepath = data.getData().getPath();
                    DialogFragment fragment = FileTypeFragment.newInstance(savepath);
                    fragment.show(getFragmentManager(), "missiles");
                }
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(RenameDialogFragment dialog) {
        Bundle args = dialog.getArguments();
        int ind = args.getInt(ARG_POSITION);
        NoticeItem renamingItem = noticeTreeAdapter.getItem(ind);
        renamingItem.setTitle(dialog.getNoticeName().getText().toString());
        current.getChildren().set(ind, renamingItem);
        noticeTreeAdapter.set(ind, renamingItem);
        noticeTreeAdapter.notifyItemChanged(ind);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_TREE, current);
        outState.putString(SAVE_FILE, path);
    }

    @Override
    public void onFragmentInteraction(String path, ExportStrategy strategy) {
        try {
            savepath = path;
            exportDocument(savingItem, savepath, strategy);
            currentExportStrategy = strategy;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMenu() {
        isMenuShown = true;
        FrameLayout.LayoutParams noticeParams = (FrameLayout.LayoutParams) addNoticeButton.getLayoutParams();
        noticeParams.rightMargin += (int) (addNoticeButton.getWidth() * 0.25);
        noticeParams.bottomMargin += (int) (addNoticeButton.getHeight() * 1.7);
        addNoticeButton.setLayoutParams(noticeParams);
        addNoticeButton.startAnimation(showNoticeButton);
        addNoticeButton.setClickable(true);
        FrameLayout.LayoutParams branchParams = (FrameLayout.LayoutParams) addBranchButton.getLayoutParams();
        branchParams.rightMargin += (int) (addBranchButton.getWidth() * 0.25);
        branchParams.bottomMargin += (int) (addBranchButton.getHeight() * 3);
        addBranchButton.setLayoutParams(branchParams);
        addBranchButton.startAnimation(showBranchButton);
        addBranchButton.setClickable(true);
    }

    private void hideMenu() {
        isMenuShown = false;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) addNoticeButton.getLayoutParams();
        layoutParams.rightMargin -= (int) (addNoticeButton.getWidth() * 0.25);
        layoutParams.bottomMargin -= (int) (addNoticeButton.getHeight() * 1.7);
        addNoticeButton.setLayoutParams(layoutParams);
        addNoticeButton.startAnimation(hideNoticeButton);
        addNoticeButton.setClickable(false);
        FrameLayout.LayoutParams branchParams = (FrameLayout.LayoutParams) addBranchButton.getLayoutParams();
        branchParams.rightMargin -= (int) (addBranchButton.getWidth() * 0.25);
        branchParams.bottomMargin -= (int) (addBranchButton.getHeight() * 3);
        addBranchButton.setLayoutParams(branchParams);
        addBranchButton.startAnimation(hideBranchButton);
        addBranchButton.setClickable(false);
    }

    private int fetchSelectionColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorSelection });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}
