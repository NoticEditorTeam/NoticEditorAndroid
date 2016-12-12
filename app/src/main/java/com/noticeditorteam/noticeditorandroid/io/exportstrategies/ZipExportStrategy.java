package com.noticeditorteam.noticeditorandroid.io.exportstrategies;

import com.noticeditorteam.noticeditorandroid.exceptions.ExportException;
import com.noticeditorteam.noticeditorandroid.io.ZipWithIndexFormat;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import net.lingala.zip4j.exception.ZipException;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class ZipExportStrategy implements ExportStrategy {

    @Override
    public boolean export(File file, NoticeItem tree) throws ExportException {
        try {
            if(file.exists()) file.delete();
            ZipWithIndexFormat.with(file).export(tree);
            return true;
        } catch (ZipException | IOException | JSONException e) {
            throw new ExportException(e);
        }
    }
}
