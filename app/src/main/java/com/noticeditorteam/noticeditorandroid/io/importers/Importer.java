package com.noticeditorteam.noticeditorandroid.io.importers;

public interface Importer<D, O, R> {

    public void importFrom(D data, O options, ImportCallback<R, Exception> callback);

}
