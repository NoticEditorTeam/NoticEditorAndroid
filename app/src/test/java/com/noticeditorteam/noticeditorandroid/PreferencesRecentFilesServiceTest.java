package com.noticeditorteam.noticeditorandroid;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by smakarov on 26.07.17.
 */
public class PreferencesRecentFilesServiceTest {

    private SharedPreferences preferences;
    private Set<String> prefsSet;

    @Test
    public void addFile() throws Exception {
        prefsSet.clear();
        Set<String> testSet = new LinkedHashSet<>();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        String path = "path";
        testSet.add(path);
        service.addFile(path);
        assertEquals("Incorrect file adding", testSet, service.getAllFiles());
    }

    @Test
    public void removeFile() throws Exception {
        prefsSet.clear();
        Set<String> testSet = new LinkedHashSet<>();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        String path = "path";
        service.addFile(path);
        service.removeFile(path);
        assertEquals("Incorrect file removing", testSet, service.getAllFiles());
    }

    @Test
    public void getAllFiles() throws Exception {
        prefsSet.clear();
        Set<String> testSet = new LinkedHashSet<>();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        String path = "path";
        testSet.add(path);
        service.addFile(path);
        assertEquals("Incorrect files getting", testSet, service.getAllFiles());
    }

    @Test
    public void addAll() throws Exception {
        prefsSet.clear();
        String[] strs = {"test1", "test2", "test3"};
        ArrayList<String> testArray = new ArrayList<>(Arrays.asList(strs));
        Set<String> testSet = new LinkedHashSet<>();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        testSet.addAll(testArray);
        service.addAll(testArray);
        assertEquals("Incorrect multiple files adding", testSet, service.getAllFiles());
    }

    @Test
    public void removeAll() throws Exception {
        prefsSet.clear();
        String[] strs = {"test1", "test2", "test3"};
        ArrayList<String> testArray = new ArrayList<>(Arrays.asList(strs));
        Set<String> testSet = new LinkedHashSet<>();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        service.addAll(testArray);
        service.removeAll(testArray);
        assertEquals("Incorrect multiple files removing", testSet, service.getAllFiles());
    }

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() throws Exception {
        preferences = Mockito.mock(SharedPreferences.class);
        prefsSet = new LinkedHashSet<>();
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(preferences.getStringSet(anyString(), anySet())).thenReturn(prefsSet);
        when(preferences.edit()).thenReturn(editor);
        when(editor.putStringSet(anyString(), anySet())).thenAnswer(i -> {
            Set<String> addingSet = i.getArgument(1);
            prefsSet = addingSet;
            return null;
        });
    }
}