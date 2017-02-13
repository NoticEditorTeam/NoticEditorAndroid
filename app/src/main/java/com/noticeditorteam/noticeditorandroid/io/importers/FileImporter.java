package com.noticeditorteam.noticeditorandroid.io.importers;

import com.noticeditorteam.noticeditorandroid.io.IOUtil;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;
import java.io.IOException;

public final class FileImporter {

    private static Tree treeImporter;
    private static Content contentImporter;

    public static Tree tree() {
        if (treeImporter == null) {
            treeImporter = new Tree();
        }
        return treeImporter;
    }

    public static Content content() {
        if (contentImporter == null) {
            contentImporter = new Content();
        }
        return contentImporter;
    }

    public static class Tree implements Importer<File, Void, NoticeItem> {

        @Override
        public void importFrom(File file, Void options, ImportCallback<NoticeItem, Exception> callback) {
            try {
                callback.call(importFrom(file), null);
            } catch (IOException ex) {
                callback.call(null, ex);
            }
        }

        public static NoticeItem importFrom(File file) throws IOException {
            return new NoticeItem(file.getName(), IOUtil.readContent(file));
        }
    }

    public static class Content implements Importer<File, Void, String> {

        @Override
        public void importFrom(File file, Void options, ImportCallback<String, Exception> callback) {
            try {
                callback.call(IOUtil.readContent(file), null);
            } catch (IOException ex) {
                callback.call(null, ex);
            }
        }
    }
}
