package com.noticeditorteam.noticeditorandroid.io.exportstrategies;

@SuppressWarnings("WeakerAccess")
public final class ExportStrategyHolder {

    public static final ZipExportStrategy ZIP = new ZipExportStrategy();
    public static final JSONExportStrategy JSON = new JSONExportStrategy();

    public static ExportStrategy[] values() {
        return new ExportStrategy[]{ZIP, JSON};
    }
}
