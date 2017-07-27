package com.noticeditorteam.noticeditorandroid;

import android.content.SharedPreferences;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by smakarov on 26.07.17.
 */

public class PreferencesRecentFilesService implements RecentFilesService {

    private static final String REC_FILES = "recent";

    private SharedPreferences preferences;

    private PreferencesRecentFilesService(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static PreferencesRecentFilesService with(SharedPreferences prefs) {
        return new PreferencesRecentFilesService(prefs);
    }

    @Override
    public void addFile(String path) {
        Set<String> recents = new LinkedHashSet<>();
        recents = preferences.getStringSet(REC_FILES, recents);
        recents.add(path);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(REC_FILES, recents);
        editor.apply();
    }

    @Override
    public void removeFile(String path) {
        Set<String> recents = new LinkedHashSet<>();
        recents = preferences.getStringSet(REC_FILES, recents);
        recents.remove(path);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(REC_FILES, recents);
        editor.apply();
    }

    @Override
    public Set<String> getAllFiles() {
        Set<String> recents = new LinkedHashSet<>();
        recents = preferences.getStringSet(REC_FILES, recents);
        return recents;
    }

    @Override
    public void addAll(Collection<String> collection) {
        Set<String> recents = new LinkedHashSet<>();
        recents = preferences.getStringSet(REC_FILES, recents);
        recents.addAll(collection);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(REC_FILES, recents);
        editor.apply();
    }

    @Override
    public void removeAll(Collection<String> collection) {
        Set<String> recents = new LinkedHashSet<>();
        recents = preferences.getStringSet(REC_FILES, recents);
        recents.removeAll(collection);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(REC_FILES, recents);
        editor.apply();
    }
}
