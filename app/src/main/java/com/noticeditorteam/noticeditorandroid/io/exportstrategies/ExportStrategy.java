package com.noticeditorteam.noticeditorandroid.io.exportstrategies;

import com.noticeditorteam.noticeditorandroid.exceptions.ExportException;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import java.io.File;

public interface ExportStrategy {

    boolean export(File file, NoticeItem tree) throws ExportException;
    String getFormatName();
    String getFileExtension();
}
