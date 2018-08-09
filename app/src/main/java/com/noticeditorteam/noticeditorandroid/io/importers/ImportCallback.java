package com.noticeditorteam.noticeditorandroid.io.importers;

@FunctionalInterface
public interface ImportCallback<R, O> {
    void call(R result, O optional);
}
