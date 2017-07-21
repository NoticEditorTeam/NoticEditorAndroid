package com.noticeditorteam.noticeditorandroid.io.exportstrategies;

import com.noticeditorteam.noticeditorandroid.exceptions.ExportException;
import com.noticeditorteam.noticeditorandroid.io.JSONFormat;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

class JSONExportStrategy implements ExportStrategy {
    @Override
    public boolean export(File file, NoticeItem tree) throws ExportException {
        try {
            JSONFormat.with(file).export(tree);
            return true;
        } catch (IOException | JSONException e) {
            throw new ExportException(e);
        }
    }

    @Override
    public String getFormatName() {
        return "JSON";
    }

    @Override
    public String getFileExtension() {
        return ".json";
    }
}
