package com.noticeditorteam.noticeditorandroid;

import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by smakarov on 27.07.17.
 */
public class PreferencesRecentFilesServiceTest {

    private final String CONFIG = "cfg";
    private final String RECENT = "recent";
    private final int MODE = 0;

    private SharedPreferences preferences;
    private Set<String> testSet;

    @Before
    public void setUp() throws Exception {
        preferences = InstrumentationRegistry.getContext().getSharedPreferences(CONFIG, MODE);
        testSet = new LinkedHashSet<>();
    }

    @Test
    public void addFile() throws Exception {
        testSet.clear();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        String path = "path";
        testSet.add(path);
        service.addFile(path);
        assertEquals("Incorrect file adding", testSet, service.getAllFiles());
    }

    @Test
    public void removeFile() throws Exception {
        testSet.clear();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        String path = "path";
        service.addFile(path);
        service.removeFile(path);
        assertEquals("Incorrect file removing", testSet, service.getAllFiles());
    }

    @Test
    public void getAllFiles() throws Exception {
        testSet.clear();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        String path = "path";
        testSet.add(path);
        service.addFile(path);
        assertEquals("Incorrect files getting", testSet, service.getAllFiles());
    }

    @Test
    public void addAll() throws Exception {
        testSet.clear();
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
        testSet.clear();
        String[] strs = {"test1", "test2", "test3"};
        ArrayList<String> testArray = new ArrayList<>(Arrays.asList(strs));
        Set<String> testSet = new LinkedHashSet<>();
        RecentFilesService service = PreferencesRecentFilesService.with(preferences);
        service.addAll(testArray);
        service.removeAll(testArray);
        assertEquals("Incorrect multiple files removing", testSet, service.getAllFiles());
    }

    @After
    public void tearDown() throws Exception {
        testSet.clear();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(RECENT, testSet);
        editor.apply();
    }

}