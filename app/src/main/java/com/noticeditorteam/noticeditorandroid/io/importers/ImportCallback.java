package com.noticeditorteam.noticeditorandroid.io.importers;

@FunctionalInterface
public interface ImportCallback<R, O> {

    public void call(R result, O optional);
}
