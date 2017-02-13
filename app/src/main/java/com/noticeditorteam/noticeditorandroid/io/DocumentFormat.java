package com.noticeditorteam.noticeditorandroid.io;

import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategy;
import com.noticeditorteam.noticeditorandroid.io.importers.FileImporter;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import net.lingala.zip4j.exception.ZipException;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class DocumentFormat {

    public static NoticeItem open(File file) throws IOException {
        final boolean isZip = file.getName().toLowerCase().endsWith(".zip");
        try {
            if (isZip) {
                return ZipWithIndexFormat.with(file).importDocument();
            }
            return JsonFormat.with(file).importDocument();
        } catch (ZipException | IOException | JSONException e){
            return FileImporter.Tree.importFrom(file);
        }
    }

    public static boolean save(NoticeItem tree, File file, ExportStrategy strategy) {
        return strategy.export(file, tree);
    }
}
