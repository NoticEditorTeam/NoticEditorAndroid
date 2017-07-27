package com.noticeditorteam.noticeditorandroid;


import java.util.Collection;
import java.util.Set;

/**
 * Created by smakarov on 26.07.17.
 */

public interface RecentFilesService {
    void addFile(String path);
    void removeFile(String path);
    Set<String> getAllFiles();
    void addAll(Collection<String> collection);
    void removeAll(Collection<String> collection);

}
